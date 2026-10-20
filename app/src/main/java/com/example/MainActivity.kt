package com.example

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.theme.StudioBorderDark
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioIndigo
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.ProjectFile
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.ExportSheet
import com.example.ui.components.FileExplorerSheet
import com.example.ui.components.HelpTutorialDialog
import com.example.ui.components.NewProjectDialog
import com.example.ui.components.SnapshotsSheet
import com.example.ui.components.SplashScreen
import com.example.ui.editor.EditorScreen
import com.example.ui.home.HomeScreen
import com.example.ui.preview.PreviewScreen
import com.example.ui.projects.ProjectsScreen
import com.example.ui.settings.SettingsScreen
import com.example.ui.theme.StudioBorderDark
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioIndigo
import com.example.ui.theme.StudioTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val themeMode by viewModel.themeMode.collectAsState()
            val isDarkTheme = when (themeMode) {
                "light" -> false
                "dark" -> true
                else -> isSystemInDarkTheme()
            }

            StudioTheme(darkTheme = isDarkTheme) {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val context = LocalContext.current
    val view = LocalView.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val projects by viewModel.projects.collectAsState()
    val activeProject by viewModel.activeProject.collectAsState()
    val activeFiles by viewModel.activeFiles.collectAsState()
    val selectedFileName by viewModel.selectedFileName.collectAsState()
    val codeFieldValue by viewModel.codeFieldValue.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val hasUnsavedChanges by viewModel.hasUnsavedChanges.collectAsState()
    val consoleLogs by viewModel.consoleLogs.collectAsState()
    val snapshots by viewModel.snapshots.collectAsState()

    val devicePreset by viewModel.devicePreset.collectAsState()
    val orientation by viewModel.orientation.collectAsState()
    val scaleMode by viewModel.scaleMode.collectAsState()
    val safeAreaOverlay by viewModel.safeAreaOverlay.collectAsState()
    val previewDarkMode by viewModel.previewDarkMode.collectAsState()
    val fontSize by viewModel.fontSize.collectAsState()
    val tabSize by viewModel.tabSize.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val autoSaveEnabled by viewModel.autoSaveEnabled.collectAsState()

    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var showSplashScreen by remember { mutableStateOf(true) }

    // Dialog & Sheet States
    var showNewProjectDialog by remember { mutableStateOf(false) }
    var showFileExplorerSheet by remember { mutableStateOf(false) }
    var showExportSheet by remember { mutableStateOf(false) }
    var showSnapshotsSheet by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var fileToExportSingle by remember { mutableStateOf<ProjectFile?>(null) }

    // Storage Access Framework Launchers
    val exportZipLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip")
    ) { uri ->
        if (uri != null) {
            viewModel.exportProjectZip(context, uri) { success, err ->
                coroutineScope.launch {
                    if (success) snackbarHostState.showSnackbar("Project ZIP exported successfully!")
                    else snackbarHostState.showSnackbar("Export failed: $err")
                }
            }
        }
    }

    val exportSingleFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*")
    ) { uri ->
        val file = fileToExportSingle
        if (uri != null && file != null) {
            viewModel.exportSingleFile(context, file, uri) { success, err ->
                coroutineScope.launch {
                    if (success) snackbarHostState.showSnackbar("Saved ${file.fileName}!")
                    else snackbarHostState.showSnackbar("Failed: $err")
                }
            }
        }
    }

    val importZipLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            viewModel.importProjectZip(context, uri) { success, err ->
                coroutineScope.launch {
                    if (success) {
                        snackbarHostState.showSnackbar("Project imported successfully!")
                        currentScreen = Screen.EDITOR
                    } else {
                        snackbarHostState.showSnackbar("Import failed: $err")
                    }
                }
            }
        }
    }

    // Auto-select first project for editor if none selected
    LaunchedEffect(projects, currentScreen) {
        if (currentScreen == Screen.EDITOR && activeProject == null && projects.isNotEmpty()) {
            viewModel.selectProject(projects.first().id)
        }
    }

    // Android Back Handler
    BackHandler {
        if (showSplashScreen) {
            showSplashScreen = false
            return@BackHandler
        }
        when (currentScreen) {
            Screen.PREVIEW -> currentScreen = Screen.EDITOR
            Screen.EDITOR -> {
                viewModel.saveCurrentFileImmediately()
                currentScreen = Screen.HOME
            }
            Screen.PROJECTS, Screen.SETTINGS -> currentScreen = Screen.HOME
            Screen.HOME -> {
                (context as? ComponentActivity)?.finish()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (currentScreen in Screen.bottomNavScreens) {
                // Floating Glass Bottom Navigation Bar (60dp, 22dp radius)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = Color(0xFF151926).copy(alpha = 0.88f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark),
                        shadowElevation = 16.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .shadow(16.dp, RoundedCornerShape(22.dp), ambientColor = Color.Black.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Screen.bottomNavScreens.forEach { screen ->
                                val selected = currentScreen == screen
                                Surface(
                                    onClick = {
                                        if (screen == Screen.EDITOR) {
                                            if (activeProject == null && projects.isNotEmpty()) {
                                                viewModel.selectProject(projects.first().id)
                                            }
                                        }
                                        currentScreen = screen
                                    },
                                    color = if (selected) StudioIndigo.copy(alpha = 0.2f) else Color.Transparent,
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.padding(vertical = 6.dp).testTag("nav_tab_${screen.name.lowercase()}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = screen.icon,
                                            contentDescription = screen.title,
                                            tint = if (selected) StudioCyan else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        if (selected) {
                                            Text(
                                                text = screen.title,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = StudioCyan
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                Screen.HOME -> {
                    HomeScreen(
                        projects = projects,
                        onOpenProject = { id ->
                            viewModel.selectProject(id)
                            currentScreen = Screen.EDITOR
                        },
                        onCreateNewProject = { showNewProjectDialog = true },
                        onUseTemplate = { template ->
                            viewModel.createNewProject(template.name, template.id) {
                                currentScreen = Screen.EDITOR
                            }
                        },
                        onImportZip = {
                            importZipLauncher.launch(
                                arrayOf("application/zip", "application/x-zip-compressed", "application/octet-stream", "*/*")
                            )
                        },
                        onToggleStar = { id -> viewModel.toggleProjectStar(id) },
                        onDuplicateProject = { id ->
                            viewModel.duplicateProject(id) {
                                coroutineScope.launch { snackbarHostState.showSnackbar("Project duplicated") }
                            }
                        },
                        onDeleteProject = { id ->
                            viewModel.deleteProject(id)
                            coroutineScope.launch { snackbarHostState.showSnackbar("Project deleted") }
                        }
                    )
                }

                Screen.EDITOR -> {
                    EditorScreen(
                        project = activeProject,
                        files = activeFiles,
                        selectedFileName = selectedFileName,
                        codeFieldValue = codeFieldValue,
                        isSaving = isSaving,
                        hasUnsavedChanges = hasUnsavedChanges,
                        fontSizeSp = fontSize,
                        onBack = {
                            viewModel.saveCurrentFileImmediately()
                            currentScreen = Screen.HOME
                        },
                        onSwitchFile = { fileName -> viewModel.switchFile(fileName) },
                        onCodeChange = { newVal -> viewModel.updateCode(newVal) },
                        onSaveFile = { viewModel.saveCurrentFileImmediately() },
                        onUndo = { viewModel.undo() },
                        onRedo = { viewModel.redo() },
                        onFormatCode = { viewModel.formatCurrentCode() },
                        onOpenPreview = {
                            viewModel.createSnapshotOnRun()
                            currentScreen = Screen.PREVIEW
                        },
                        onOpenFileExplorer = { showFileExplorerSheet = true },
                        onOpenExport = { showExportSheet = true },
                        onInsertSnippet = { html, css -> viewModel.insertComponentSnippet(html, css) },
                        onApplyDesignTokens = { tokens -> viewModel.applyDesignTokens(tokens) },
                        onReplaceColorToken = { range, hex -> viewModel.replaceColorToken(range, hex) },
                        onOpenSnapshots = { showSnapshotsSheet = true },
                        onImportAsset = { name, dataUri -> viewModel.addNewFile(name, dataUri) }
                    )
                }

                Screen.PREVIEW -> {
                    PreviewScreen(
                        project = activeProject,
                        files = activeFiles,
                        devicePreset = devicePreset,
                        orientation = orientation,
                        scaleMode = scaleMode,
                        safeAreaOverlay = safeAreaOverlay,
                        previewDarkMode = previewDarkMode,
                        consoleLogs = consoleLogs,
                        onBackToEditor = { currentScreen = Screen.EDITOR },
                        onSetDevicePreset = { preset -> viewModel.setDevicePreset(preset) },
                        onToggleOrientation = { viewModel.toggleOrientation() },
                        onSetScaleMode = { mode -> viewModel.setScaleMode(mode) },
                        onToggleSafeArea = { viewModel.toggleSafeAreaOverlay() },
                        onTogglePreviewDarkMode = { viewModel.togglePreviewDarkMode() },
                        onAddLog = { log -> viewModel.addConsoleLog(log) },
                        onClearLogs = { viewModel.clearConsoleLogs() }
                    )
                }

                Screen.PROJECTS -> {
                    ProjectsScreen(
                        projects = projects,
                        onOpenProject = { id ->
                            viewModel.selectProject(id)
                            currentScreen = Screen.EDITOR
                        },
                        onCreateNewProject = { showNewProjectDialog = true },
                        onDuplicateProject = { id ->
                            viewModel.duplicateProject(id) {
                                coroutineScope.launch { snackbarHostState.showSnackbar("Project duplicated") }
                            }
                        },
                        onRenameProject = { id, newName -> viewModel.renameProject(id, newName) },
                        onToggleStar = { id -> viewModel.toggleProjectStar(id) },
                        onDeleteProject = { id ->
                            viewModel.deleteProject(id)
                            coroutineScope.launch { snackbarHostState.showSnackbar("Project deleted") }
                        }
                    )
                }

                Screen.SETTINGS -> {
                    SettingsScreen(
                        themeMode = themeMode,
                        fontSize = fontSize,
                        tabSize = tabSize,
                        autoSaveEnabled = autoSaveEnabled,
                        projectsCount = projects.size,
                        onSetThemeMode = { mode -> viewModel.setThemeMode(mode) },
                        onSetFontSize = { size -> viewModel.setFontSize(size) },
                        onSetTabSize = { size -> viewModel.setTabSize(size) },
                        onToggleAutoSave = { enabled -> viewModel.toggleAutoSave(enabled) },
                        onOpenHelp = { showHelpDialog = true },
                        onShowSplash = { showSplashScreen = true },
                        onReseedDefaults = {
                            coroutineScope.launch {
                                for (t in listOf("blank", "login", "dashboard", "chat", "music")) {
                                    viewModel.createNewProject("Sample ${t.replaceFirstChar { it.uppercase() }}", t) {}
                                }
                                snackbarHostState.showSnackbar("Starter templates restored!")
                            }
                        }
                    )
                }
            }
        }
    }

    // New Project Dialog
    if (showNewProjectDialog) {
        NewProjectDialog(
            onDismiss = { showNewProjectDialog = false },
            onCreateProject = { name, templateId, folder ->
                showNewProjectDialog = false
                viewModel.createNewProject(name, templateId, folder = folder) {
                    currentScreen = Screen.EDITOR
                }
            }
        )
    }

    // File Explorer Bottom Sheet
    if (showFileExplorerSheet) {
        FileExplorerSheet(
            files = activeFiles,
            currentFileName = selectedFileName,
            onSelectFile = { fileName -> viewModel.switchFile(fileName) },
            onAddNewFile = { fileName -> viewModel.addNewFile(fileName) },
            onDeleteFile = { fileName -> viewModel.deleteFile(fileName) },
            onDismiss = { showFileExplorerSheet = false }
        )
    }

    // Snapshots Version History Sheet
    if (showSnapshotsSheet) {
        SnapshotsSheet(
            snapshots = snapshots,
            onRestoreSnapshot = { id -> viewModel.restoreSnapshot(id) },
            onCreateSnapshot = { viewModel.createManualSnapshot("Manual snapshot") },
            onDismiss = { showSnapshotsSheet = false }
        )
    }

    // Export Center Dialog with PNG/JPG/WebP/ZIP
    if (showExportSheet) {
        val proj = activeProject
        val curFile = activeFiles.find { it.fileName == selectedFileName }
        ExportSheet(
            projectName = proj?.name ?: "Project",
            currentFileName = selectedFileName,
            onExportZip = {
                val cleanName = (proj?.name ?: "mobile_ui_studio").replace(" ", "_").lowercase()
                exportZipLauncher.launch("$cleanName.zip")
            },
            onExportCurrentFile = {
                if (curFile != null) {
                    fileToExportSingle = curFile
                    exportSingleFileLauncher.launch(curFile.fileName)
                }
            },
            onExportImage = { format, scale, transparent, shareDirectly ->
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        val bitmap = Bitmap.createBitmap(
                            (view.width * scale).coerceAtLeast(1),
                            (view.height * scale).coerceAtLeast(1),
                            Bitmap.Config.ARGB_8888
                        )
                        val canvas = Canvas(bitmap)
                        if (!transparent) {
                            canvas.drawColor(android.graphics.Color.parseColor("#07080C"))
                        }
                        canvas.scale(scale.toFloat(), scale.toFloat())
                        view.draw(canvas)

                        val ext = when (format.uppercase()) {
                            "JPG" -> "jpg"
                            "WEBP" -> "webp"
                            else -> "png"
                        }
                        val compressFormat = when (format.uppercase()) {
                            "JPG" -> Bitmap.CompressFormat.JPEG
                            "WEBP" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Bitmap.CompressFormat.WEBP_LOSSY else Bitmap.CompressFormat.WEBP
                            else -> Bitmap.CompressFormat.PNG
                        }

                        if (shareDirectly) {
                            val tempFile = File(context.cacheDir, "mockup_${System.currentTimeMillis()}.$ext")
                            FileOutputStream(tempFile).use { fos ->
                                bitmap.compress(compressFormat, 95, fos)
                            }
                            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", tempFile)
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "image/$ext"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            withContext(Dispatchers.Main) {
                                context.startActivity(Intent.createChooser(shareIntent, "Share Mockup Image"))
                            }
                        } else {
                            val filename = "UI_Studio_${System.currentTimeMillis()}.$ext"
                            var fos: OutputStream? = null
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                val resolver = context.contentResolver
                                val contentValues = ContentValues().apply {
                                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                                    put(MediaStore.MediaColumns.MIME_TYPE, "image/$ext")
                                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MobileUIStudio")
                                }
                                val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                                if (imageUri != null) fos = resolver.openOutputStream(imageUri)
                            } else {
                                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                val image = File(imagesDir, filename)
                                fos = FileOutputStream(image)
                            }

                            fos?.use {
                                bitmap.compress(compressFormat, 95, it)
                            }

                            withContext(Dispatchers.Main) {
                                snackbarHostState.showSnackbar("Mockup ($format) saved to Pictures folder!")
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            snackbarHostState.showSnackbar("Export failed: ${e.message}")
                        }
                    }
                }
            },
            onShareZip = {
                if (proj != null) {
                    coroutineScope.launch(Dispatchers.IO) {
                        try {
                            val cleanName = proj.name.replace(" ", "_").lowercase()
                            val zipFile = File(context.cacheDir, "$cleanName.zip")
                            FileOutputStream(zipFile).use { fos ->
                                ZipOutputStream(fos).use { zos ->
                                    for (f in activeFiles) {
                                        zos.putNextEntry(ZipEntry(f.fileName))
                                        zos.write(f.content.toByteArray(Charsets.UTF_8))
                                        zos.closeEntry()
                                    }
                                }
                            }
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                zipFile
                            )
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/zip"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            withContext(Dispatchers.Main) {
                                context.startActivity(Intent.createChooser(shareIntent, "Share Project ZIP"))
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Share failed: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            },
            onDismiss = { showExportSheet = false }
        )
    }

    // Help & Tutorial Dialog
    if (showHelpDialog) {
        HelpTutorialDialog(onDismiss = { showHelpDialog = false })
    }

    if (showSplashScreen) {
        SplashScreen(
            onDismiss = { showSplashScreen = false }
        )
    }
}
}
