package com.example.ui

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.StudioDatabase
import com.example.data.model.FileType
import com.example.data.model.Project
import com.example.data.model.ProjectFile
import com.example.data.model.ProjectSnapshot
import com.example.data.repository.ProjectRepository
import com.example.editor.CodeFormatter
import com.example.editor.UndoRedoManager
import com.example.preview.ConsoleLogItem
import com.example.preview.DevicePreset
import com.example.preview.Orientation
import com.example.preview.ScaleMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProjectRepository

    init {
        val database = StudioDatabase.getInstance(application)
        repository = ProjectRepository(database.projectDao())
        viewModelScope.launch {
            repository.checkAndSeedInitialProjects()
        }
    }

    val projects: StateFlow<List<Project>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val starredProjects: StateFlow<List<Project>> = repository.starredProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeProjectId = MutableStateFlow<String?>(null)
    val activeProjectId: StateFlow<String?> = _activeProjectId.asStateFlow()

    private val _activeProject = MutableStateFlow<Project?>(null)
    val activeProject: StateFlow<Project?> = _activeProject.asStateFlow()

    private val _activeFiles = MutableStateFlow<List<ProjectFile>>(emptyList())
    val activeFiles: StateFlow<List<ProjectFile>> = _activeFiles.asStateFlow()

    private val _selectedFileName = MutableStateFlow("index.html")
    val selectedFileName: StateFlow<String> = _selectedFileName.asStateFlow()

    private val _codeFieldValue = MutableStateFlow(TextFieldValue(""))
    val codeFieldValue: StateFlow<TextFieldValue> = _codeFieldValue.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _hasUnsavedChanges = MutableStateFlow(false)
    val hasUnsavedChanges: StateFlow<Boolean> = _hasUnsavedChanges.asStateFlow()

    private val _consoleLogs = MutableStateFlow<List<ConsoleLogItem>>(emptyList())
    val consoleLogs: StateFlow<List<ConsoleLogItem>> = _consoleLogs.asStateFlow()

    private val _snapshots = MutableStateFlow<List<ProjectSnapshot>>(emptyList())
    val snapshots: StateFlow<List<ProjectSnapshot>> = _snapshots.asStateFlow()

    // Editor Settings
    private val _fontSize = MutableStateFlow(14)
    val fontSize: StateFlow<Int> = _fontSize.asStateFlow()

    private val _tabSize = MutableStateFlow(2)
    val tabSize: StateFlow<Int> = _tabSize.asStateFlow()

    private val _themeMode = MutableStateFlow("dark") // "dark", "light", "system"
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _autoSaveEnabled = MutableStateFlow(true)
    val autoSaveEnabled: StateFlow<Boolean> = _autoSaveEnabled.asStateFlow()

    // Preview Viewport Settings
    private val _devicePreset = MutableStateFlow(DevicePreset.STANDARD)
    val devicePreset: StateFlow<DevicePreset> = _devicePreset.asStateFlow()

    private val _orientation = MutableStateFlow(Orientation.PORTRAIT)
    val orientation: StateFlow<Orientation> = _orientation.asStateFlow()

    private val _scaleMode = MutableStateFlow(ScaleMode.FIT)
    val scaleMode: StateFlow<ScaleMode> = _scaleMode.asStateFlow()

    private val _safeAreaOverlay = MutableStateFlow(true)
    val safeAreaOverlay: StateFlow<Boolean> = _safeAreaOverlay.asStateFlow()

    private val _isSplitView = MutableStateFlow(false)
    val isSplitView: StateFlow<Boolean> = _isSplitView.asStateFlow()

    private val _previewDarkMode = MutableStateFlow(true)
    val previewDarkMode: StateFlow<Boolean> = _previewDarkMode.asStateFlow()

    val undoRedoManager = UndoRedoManager()
    private var autoSaveJob: Job? = null
    private var projectFilesJob: Job? = null
    private var snapshotsJob: Job? = null

    fun selectProject(projectId: String) {
        if (_activeProjectId.value == projectId) return
        _activeProjectId.value = projectId

        projectFilesJob?.cancel()
        projectFilesJob = viewModelScope.launch {
            repository.getProjectById(projectId).collect { proj ->
                _activeProject.value = proj
            }
        }

        snapshotsJob?.cancel()
        snapshotsJob = viewModelScope.launch {
            repository.getSnapshots(projectId).collect { list ->
                _snapshots.value = list
            }
        }

        viewModelScope.launch {
            repository.getProjectFiles(projectId).collect { files ->
                _activeFiles.value = files
                val currentFile = files.find { it.fileName == _selectedFileName.value }
                    ?: files.find { it.fileName.equals("index.html", ignoreCase = true) }
                    ?: files.firstOrNull()

                if (currentFile != null) {
                    if (_selectedFileName.value != currentFile.fileName || _codeFieldValue.value.text.isEmpty()) {
                        _selectedFileName.value = currentFile.fileName
                        _codeFieldValue.value = TextFieldValue(currentFile.content, TextRange(currentFile.content.length))
                        undoRedoManager.reset(currentFile.content, _codeFieldValue.value.selection)
                        _hasUnsavedChanges.value = false
                    }
                }
            }
        }
    }

    fun switchFile(fileName: String) {
        if (_selectedFileName.value == fileName) return
        if (_hasUnsavedChanges.value) {
            saveCurrentFileImmediately()
        }

        val targetFile = _activeFiles.value.find { it.fileName == fileName }
        if (targetFile != null) {
            _selectedFileName.value = fileName
            _codeFieldValue.value = TextFieldValue(targetFile.content, TextRange(targetFile.content.length))
            undoRedoManager.reset(targetFile.content, _codeFieldValue.value.selection)
            _hasUnsavedChanges.value = false
        }
    }

    fun updateCode(newValue: TextFieldValue) {
        val oldText = _codeFieldValue.value.text
        _codeFieldValue.value = newValue

        if (oldText != newValue.text) {
            _hasUnsavedChanges.value = true
            undoRedoManager.recordChange(newValue.text, newValue.selection)

            if (_autoSaveEnabled.value) {
                autoSaveJob?.cancel()
                autoSaveJob = viewModelScope.launch {
                    delay(800)
                    saveCurrentFileImmediately()
                }
            }
        }
    }

    fun undo() {
        val snapshot = undoRedoManager.undo()
        if (snapshot != null) {
            _codeFieldValue.value = TextFieldValue(snapshot.text, snapshot.selection)
            _hasUnsavedChanges.value = true
        }
    }

    fun redo() {
        val snapshot = undoRedoManager.redo()
        if (snapshot != null) {
            _codeFieldValue.value = TextFieldValue(snapshot.text, snapshot.selection)
            _hasUnsavedChanges.value = true
        }
    }

    fun formatCurrentCode() {
        val currentFile = _activeFiles.value.find { it.fileName == _selectedFileName.value }
        val fileType = currentFile?.fileType ?: FileType.fromFileName(_selectedFileName.value)
        val formatted = CodeFormatter.format(_codeFieldValue.value.text, fileType, _tabSize.value)
        updateCode(_codeFieldValue.value.copy(text = formatted))
    }

    fun saveCurrentFileImmediately() {
        val projectId = _activeProjectId.value ?: return
        val fileName = _selectedFileName.value
        val content = _codeFieldValue.value.text

        viewModelScope.launch {
            _isSaving.value = true
            repository.saveFile(projectId, fileName, content)
            delay(300)
            _isSaving.value = false
            _hasUnsavedChanges.value = false
        }
    }

    fun createSnapshotOnRun() {
        val projectId = _activeProjectId.value ?: return
        viewModelScope.launch {
            saveCurrentFileImmediately()
            repository.createSnapshot(projectId, "Run snapshot")
        }
    }

    fun createManualSnapshot(label: String) {
        val projectId = _activeProjectId.value ?: return
        viewModelScope.launch {
            saveCurrentFileImmediately()
            repository.createSnapshot(projectId, label.ifBlank { "Manual snapshot" })
        }
    }

    fun restoreSnapshot(snapshotId: String) {
        viewModelScope.launch {
            repository.restoreSnapshot(snapshotId)
            val currentFile = _activeFiles.value.find { it.fileName == _selectedFileName.value }
            if (currentFile != null) {
                _codeFieldValue.value = TextFieldValue(currentFile.content)
            }
        }
    }

    fun insertComponentSnippet(html: String, css: String) {
        val currentText = _codeFieldValue.value.text
        val selection = _codeFieldValue.value.selection

        // Insert HTML at cursor if in HTML file
        if (_selectedFileName.value.endsWith(".html", ignoreCase = true)) {
            val newText = currentText.replaceRange(selection.min, selection.max, html)
            updateCode(_codeFieldValue.value.copy(text = newText, selection = TextRange(selection.min + html.length)))
        }

        // Automatically append CSS to style.css
        val projectId = _activeProjectId.value ?: return
        viewModelScope.launch {
            val styleFile = _activeFiles.value.find { it.fileName.equals("style.css", ignoreCase = true) }
            val existingCss = styleFile?.content ?: ""
            val newCss = existingCss + "\n\n/* Added from Component Palette */\n" + css
            repository.saveFile(projectId, "style.css", newCss)
        }
    }

    fun applyDesignTokens(tokensCss: String) {
        val projectId = _activeProjectId.value ?: return
        viewModelScope.launch {
            val styleFile = _activeFiles.value.find { it.fileName.equals("style.css", ignoreCase = true) }
            val existingCss = styleFile?.content ?: ""
            val cleaned = if (existingCss.contains(":root")) {
                existingCss.replace(Regex(":root\\s*\\{[^}]*\\}"), tokensCss.trim())
            } else {
                tokensCss + "\n" + existingCss
            }
            repository.saveFile(projectId, "style.css", cleaned)
            if (_selectedFileName.value == "style.css") {
                _codeFieldValue.value = TextFieldValue(cleaned)
            }
        }
    }

    fun replaceColorToken(range: IntRange, newHex: String) {
        val text = _codeFieldValue.value.text
        if (range.first in 0..text.length && range.last <= text.length) {
            val newText = text.replaceRange(range.first, range.last, newHex)
            updateCode(_codeFieldValue.value.copy(text = newText))
        }
    }

    fun toggleProjectStar(projectId: String) {
        viewModelScope.launch {
            val proj = _activeFiles.value.let {
                repository.getProjectById(projectId)
            }
            val currentStar = projects.value.find { it.id == projectId }?.isStarred ?: false
            repository.toggleStar(projectId, !currentStar)
        }
    }

    fun setProjectFolder(projectId: String, folder: String) {
        viewModelScope.launch {
            repository.setProjectFolder(projectId, folder)
        }
    }

    fun createNewProject(name: String, templateId: String, folder: String = "All", onCreated: (String) -> Unit) {
        viewModelScope.launch {
            val newId = repository.createProject(name.ifBlank { "Untitled Project" }, templateId, folder = folder)
            selectProject(newId)
            onCreated(newId)
        }
    }

    fun duplicateProject(projectId: String, onDuplicated: (String) -> Unit) {
        viewModelScope.launch {
            val newId = repository.duplicateProject(projectId)
            onDuplicated(newId)
        }
    }

    fun renameProject(projectId: String, newName: String) {
        viewModelScope.launch {
            repository.renameProject(projectId, newName)
        }
    }

    fun deleteProject(projectId: String) {
        viewModelScope.launch {
            repository.deleteProject(projectId)
            if (_activeProjectId.value == projectId) {
                _activeProjectId.value = null
                _activeProject.value = null
                _activeFiles.value = emptyList()
            }
        }
    }

    fun addNewFile(fileName: String, initialContent: String = "") {
        val projectId = _activeProjectId.value ?: return
        val cleanName = fileName.trim()
        if (cleanName.isBlank()) return

        viewModelScope.launch {
            repository.createFile(projectId, cleanName, initialContent)
            switchFile(cleanName)
        }
    }

    fun deleteFile(fileName: String) {
        val projectId = _activeProjectId.value ?: return
        if (fileName.equals("index.html", ignoreCase = true)) return

        viewModelScope.launch {
            repository.deleteFile(projectId, fileName)
            if (_selectedFileName.value == fileName) {
                switchFile("index.html")
            }
        }
    }

    fun addConsoleLog(log: ConsoleLogItem) {
        _consoleLogs.value = (_consoleLogs.value + log).takeLast(100)
    }

    fun clearConsoleLogs() {
        _consoleLogs.value = emptyList()
    }

    fun setDevicePreset(preset: DevicePreset) {
        _devicePreset.value = preset
    }

    fun toggleOrientation() {
        _orientation.value = _orientation.value.toggle()
    }

    fun setScaleMode(mode: ScaleMode) {
        _scaleMode.value = mode
    }

    fun toggleSafeAreaOverlay() {
        _safeAreaOverlay.value = !_safeAreaOverlay.value
    }

    fun toggleSplitView() {
        _isSplitView.value = !_isSplitView.value
    }

    fun togglePreviewDarkMode() {
        _previewDarkMode.value = !_previewDarkMode.value
    }

    fun setThemeMode(mode: String) {
        _themeMode.value = mode
    }

    fun setFontSize(size: Int) {
        _fontSize.value = size.coerceIn(10, 24)
    }

    fun setTabSize(size: Int) {
        _tabSize.value = size.coerceIn(2, 4)
    }

    fun toggleAutoSave(enabled: Boolean) {
        _autoSaveEnabled.value = enabled
    }

    fun exportProjectZip(context: Context, uri: Uri, onResult: (Boolean, String?) -> Unit) {
        val projectId = _activeProjectId.value ?: run {
            onResult(false, "No active project selected")
            return
        }
        viewModelScope.launch {
            val result = repository.exportProjectToZip(context, projectId, uri)
            if (result.isSuccess) {
                onResult(true, null)
            } else {
                onResult(false, result.exceptionOrNull()?.localizedMessage ?: "Failed to export ZIP")
            }
        }
    }

    fun exportSingleFile(context: Context, file: ProjectFile, uri: Uri, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = repository.exportSingleFile(context, file, uri)
            if (result.isSuccess) {
                onResult(true, null)
            } else {
                onResult(false, result.exceptionOrNull()?.localizedMessage ?: "Failed to export file")
            }
        }
    }

    fun importProjectZip(context: Context, uri: Uri, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = repository.importProjectFromZip(context, uri)
            if (result.isSuccess) {
                val newId = result.getOrNull()
                if (newId != null) {
                    selectProject(newId)
                }
                onResult(true, null)
            } else {
                onResult(false, result.exceptionOrNull()?.localizedMessage ?: "Invalid ZIP archive")
            }
        }
    }
}
