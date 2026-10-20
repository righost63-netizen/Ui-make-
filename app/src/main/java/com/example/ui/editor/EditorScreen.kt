package com.example.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FindReplace
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VerticalSplit
import androidx.compose.ui.viewinterop.AndroidView
import com.example.preview.PreviewHtmlGenerator
import com.example.ui.components.AssetManagerSheet
import com.example.ui.components.AuditSheet
import com.example.ui.theme.StudioCyan
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FileType
import com.example.data.model.Project
import com.example.data.model.ProjectFile
import com.example.data.templates.ComponentLibrary
import com.example.editor.AutocompleteManager
import com.example.editor.ColorToken
import com.example.editor.EditorUtils
import com.example.editor.ProgrammingSymbolBar
import com.example.editor.SyntaxHighlighter
import com.example.ui.components.ColorPickerDialog
import com.example.ui.components.ComponentPaletteSheet
import com.example.ui.components.DesignTokensSheet
import com.example.ui.components.GeneratorsSheet
import com.example.ui.components.SnapshotsSheet
import com.example.ui.theme.StudioBorderDark
import com.example.ui.theme.StudioSuccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    project: Project?,
    files: List<ProjectFile>,
    selectedFileName: String,
    codeFieldValue: TextFieldValue,
    isSaving: Boolean,
    hasUnsavedChanges: Boolean,
    fontSizeSp: Int,
    onBack: () -> Unit,
    onSwitchFile: (String) -> Unit,
    onCodeChange: (TextFieldValue) -> Unit,
    onSaveFile: () -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onFormatCode: () -> Unit,
    onOpenPreview: () -> Unit,
    onOpenFileExplorer: () -> Unit,
    onOpenExport: () -> Unit,
    onInsertSnippet: (html: String, css: String) -> Unit,
    onApplyDesignTokens: (String) -> Unit,
    onReplaceColorToken: (IntRange, String) -> Unit,
    onOpenSnapshots: () -> Unit,
    onImportAsset: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    var showFindReplace by remember { mutableStateOf(false) }
    var showPaletteSheet by remember { mutableStateOf(false) }
    var showGeneratorsSheet by remember { mutableStateOf(false) }
    var showDesignTokensSheet by remember { mutableStateOf(false) }
    var showAuditSheet by remember { mutableStateOf(false) }
    var showAssetSheet by remember { mutableStateOf(false) }
    var isSplitScreen by remember { mutableStateOf(false) }
    var activeColorToken by remember { mutableStateOf<ColorToken?>(null) }

    var searchQuery by remember { mutableStateOf("") }
    var replaceQuery by remember { mutableStateOf("") }
    var currentMatchIndex by remember { mutableIntStateOf(0) }

    val currentFile = files.find { it.fileName == selectedFileName }
    val fileType = currentFile?.fileType ?: FileType.fromFileName(selectedFileName)

    // Calculate line numbers
    val lineCount = remember(codeFieldValue.text) {
        maxOf(1, codeFieldValue.text.count { it == '\n' } + 1)
    }

    // Search matches
    val searchMatches = remember(codeFieldValue.text, searchQuery) {
        if (searchQuery.isNotEmpty()) {
            EditorUtils.searchMatches(codeFieldValue.text, searchQuery, matchCase = false)
        } else {
            emptyList()
        }
    }

    // Detected color tokens in current code
    val colorTokens = remember(codeFieldValue.text) {
        SyntaxHighlighter.extractColorTokens(codeFieldValue.text)
    }

    // Autocomplete suggestions
    val textBeforeCursor = remember(codeFieldValue) {
        val cursor = codeFieldValue.selection.start
        if (cursor in 0..codeFieldValue.text.length) {
            codeFieldValue.text.substring(0, cursor)
        } else ""
    }

    val suggestions = remember(textBeforeCursor, fileType) {
        AutocompleteManager.getSuggestions(textBeforeCursor, fileType)
    }

    val verticalScrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = project?.name ?: "Editor",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = selectedFileName,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        if (isSaving) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                CircularProgressIndicator(modifier = Modifier.size(10.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.primary)
                                Text("Saving...", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
                            }
                        } else if (hasUnsavedChanges) {
                            Text("• Unsaved", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary, fontSize = 10.sp)
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = StudioSuccess, modifier = Modifier.size(12.dp))
                                Text("Saved", style = MaterialTheme.typography.labelSmall, color = StudioSuccess, fontSize = 10.sp)
                            }
                        }
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack, modifier = Modifier.testTag("editor_back_btn")) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = onUndo, modifier = Modifier.testTag("editor_undo_btn")) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Undo, contentDescription = "Undo")
                }
                IconButton(onClick = onRedo, modifier = Modifier.testTag("editor_redo_btn")) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Redo, contentDescription = "Redo")
                }

                // Split Screen Mode Toggle Button
                IconButton(onClick = { isSplitScreen = !isSplitScreen }, modifier = Modifier.testTag("editor_split_btn")) {
                    Icon(
                        imageVector = Icons.Default.VerticalSplit,
                        contentDescription = "Split View",
                        tint = if (isSplitScreen) StudioCyan else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Green Glowing "Run" Button
                Button(
                    onClick = {
                        onSaveFile()
                        onOpenPreview()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StudioSuccess,
                        contentColor = Color(0xFF07080C)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .shadow(12.dp, RoundedCornerShape(12.dp), ambientColor = StudioSuccess, spotColor = StudioSuccess)
                        .testTag("editor_run_btn")
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Run", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Run", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Box {
                    IconButton(onClick = { showMenu = true }, modifier = Modifier.testTag("editor_overflow_btn")) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text(if (isSplitScreen) "Exit Split Screen" else "Split Screen View") },
                            leadingIcon = { Icon(Icons.Default.VerticalSplit, contentDescription = null, tint = StudioCyan) },
                            onClick = {
                                showMenu = false
                                isSplitScreen = !isSplitScreen
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Component Palette") },
                            leadingIcon = { Icon(Icons.Default.Category, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                showPaletteSheet = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Audit (A11y & Performance)") },
                            leadingIcon = { Icon(Icons.Default.Speed, contentDescription = null, tint = StudioSuccess) },
                            onClick = {
                                showMenu = false
                                showAuditSheet = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Media & Asset Manager") },
                            leadingIcon = { Icon(Icons.Default.Image, contentDescription = null, tint = Color(0xFFFBBF24)) },
                            onClick = {
                                showMenu = false
                                showAssetSheet = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("CSS Style Generator") },
                            leadingIcon = { Icon(Icons.Default.AutoAwesome, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                showGeneratorsSheet = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Design Tokens (:root)") },
                            leadingIcon = { Icon(Icons.Default.Palette, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                showDesignTokensSheet = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Format Code (Prettify)") },
                            leadingIcon = { Icon(Icons.Default.FormatPaint, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                onFormatCode()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Version History") },
                            leadingIcon = { Icon(Icons.Default.History, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                onOpenSnapshots()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Find & Replace") },
                            leadingIcon = { Icon(Icons.Default.FindReplace, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                showFindReplace = !showFindReplace
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("File Explorer") },
                            leadingIcon = { Icon(Icons.Default.FolderOpen, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                onOpenFileExplorer()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Export Project") },
                            leadingIcon = { Icon(Icons.Default.UploadFile, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                onOpenExport()
                            }
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        // File Tabs Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            files.forEach { file ->
                val isSelected = file.fileName == selectedFileName
                Surface(
                    onClick = { onSwitchFile(file.fileName) },
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                    modifier = Modifier.testTag("tab_${file.fileName}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val dotColor = when (file.fileType) {
                            FileType.HTML -> Color(0xFFF9A8D4)
                            FileType.CSS -> Color(0xFF7DD3FC)
                            FileType.JAVASCRIPT -> Color(0xFF86EFAC)
                            else -> MaterialTheme.colorScheme.primary
                        }
                        Box(modifier = Modifier.size(8.dp).background(dotColor, CircleShape))
                        Text(
                            text = file.fileName,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontFamily = FontFamily.Monospace,
                            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            IconButton(
                onClick = onOpenFileExplorer,
                modifier = Modifier
                    .size(30.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .testTag("tab_add_file_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = "New File", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
            }
        }

        // Inline Color Swatches Toolbar (if hex colors found)
        if (colorTokens.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Swatches:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                colorTokens.take(8).forEach { token ->
                    Surface(
                        onClick = { activeColorToken = token },
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.size(12.dp).background(token.color, CircleShape).border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape))
                            Text(token.text, fontFamily = FontFamily.Monospace, fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        // Find & Replace Panel
        if (showFindReplace) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(0.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp).testTag("find_replace_panel")
            ) {
                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it; currentMatchIndex = 0 },
                            placeholder = { Text("Find...") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Text(text = if (searchMatches.isNotEmpty()) "${currentMatchIndex + 1}/${searchMatches.size}" else "0/0", style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace)
                        IconButton(onClick = {
                            if (searchMatches.isNotEmpty()) {
                                currentMatchIndex = (currentMatchIndex - 1 + searchMatches.size) % searchMatches.size
                                val match = searchMatches[currentMatchIndex]
                                onCodeChange(codeFieldValue.copy(selection = TextRange(match.first, match.last + 1)))
                            }
                        }, enabled = searchMatches.isNotEmpty()) { Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Prev") }
                        IconButton(onClick = {
                            if (searchMatches.isNotEmpty()) {
                                currentMatchIndex = (currentMatchIndex + 1) % searchMatches.size
                                val match = searchMatches[currentMatchIndex]
                                onCodeChange(codeFieldValue.copy(selection = TextRange(match.first, match.last + 1)))
                            }
                        }, enabled = searchMatches.isNotEmpty()) { Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Next") }
                        IconButton(onClick = { showFindReplace = false }) { Icon(Icons.Default.Close, contentDescription = "Close") }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(value = replaceQuery, onValueChange = { replaceQuery = it }, placeholder = { Text("Replace with...") }, singleLine = true, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp))
                        Button(onClick = {
                            if (searchMatches.isNotEmpty() && currentMatchIndex < searchMatches.size) {
                                val match = searchMatches[currentMatchIndex]
                                val newText = codeFieldValue.text.replaceRange(match.first, match.last + 1, replaceQuery)
                                onCodeChange(codeFieldValue.copy(text = newText, selection = TextRange(match.first + replaceQuery.length)))
                            }
                        }, enabled = searchMatches.isNotEmpty(), shape = RoundedCornerShape(8.dp)) { Text("Replace") }
                        Button(onClick = {
                            if (searchMatches.isNotEmpty()) {
                                val newText = codeFieldValue.text.replace(searchQuery, replaceQuery, ignoreCase = true)
                                onCodeChange(codeFieldValue.copy(text = newText))
                            }
                        }, enabled = searchMatches.isNotEmpty(), shape = RoundedCornerShape(8.dp)) { Text("All") }
                    }
                }
            }
        }

        // Code Editor Workspace (Fullscreen or Split Screen)
        if (isSplitScreen) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Top Pane: Code Editor
                Box(
                    modifier = Modifier
                        .weight(0.52f)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(verticalScrollState)
                    ) {
                        // Line Numbers Gutter
                        Column(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                                .padding(horizontal = 8.dp, vertical = 12.dp)
                                .width(42.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            for (i in 1..lineCount) {
                                Text(
                                    text = i.toString(),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = fontSizeSp.sp,
                                    lineHeight = (fontSizeSp + 6).sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                        }

                        // Code Input with Real-time Syntax Highlighting
                        BasicTextField(
                            value = codeFieldValue,
                            onValueChange = { newValue -> onCodeChange(newValue) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp, end = 16.dp, top = 12.dp, bottom = 48.dp)
                                .testTag("code_editor_input"),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = fontSizeSp.sp,
                                lineHeight = (fontSizeSp + 6).sp,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            visualTransformation = VisualTransformation { text ->
                                val highlighted = SyntaxHighlighter.highlight(text.text, fileType)
                                TransformedText(highlighted, OffsetMapping.Identity)
                            }
                        )
                    }
                }

                // Split Divider Bar
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth().height(26.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(modifier = Modifier.size(6.dp).background(StudioSuccess, CircleShape))
                            Text("LIVE PREVIEW (REAL-TIME)", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StudioCyan)
                        }
                        IconButton(onClick = { isSplitScreen = false }, modifier = Modifier.size(20.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close Split", modifier = Modifier.size(14.dp))
                        }
                    }
                }

                // Bottom Pane: Live WebView Preview
                Box(
                    modifier = Modifier
                        .weight(0.48f)
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    AndroidView(
                        factory = { ctx ->
                            WebView(ctx).apply {
                                settings.javaScriptEnabled = true
                                settings.domStorageEnabled = true
                                settings.loadWithOverviewMode = true
                                settings.useWideViewPort = true
                                webViewClient = WebViewClient()
                            }
                        },
                        update = { wv ->
                            wv.loadDataWithBaseURL("https://local.preview", PreviewHtmlGenerator.generateCombinedHtml(files), "text/html", "UTF-8", null)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(verticalScrollState)
                ) {
                    // Line Numbers Gutter
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                            .padding(horizontal = 8.dp, vertical = 12.dp)
                            .width(42.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        for (i in 1..lineCount) {
                            Text(
                                text = i.toString(),
                                fontFamily = FontFamily.Monospace,
                                fontSize = fontSizeSp.sp,
                                lineHeight = (fontSizeSp + 6).sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                    }

                    // Code Input with Real-time Syntax Highlighting
                    BasicTextField(
                        value = codeFieldValue,
                        onValueChange = { newValue -> onCodeChange(newValue) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp, end = 16.dp, top = 12.dp, bottom = 48.dp)
                            .testTag("code_editor_input"),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = fontSizeSp.sp,
                            lineHeight = (fontSizeSp + 6).sp,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        visualTransformation = VisualTransformation { text ->
                            val highlighted = SyntaxHighlighter.highlight(text.text, fileType)
                            TransformedText(highlighted, OffsetMapping.Identity)
                        }
                    )
                }
            }
        }

        // Autocomplete Suggestion Chips (above symbol bar)
        if (suggestions.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(suggestions) { item ->
                    Surface(
                        onClick = {
                            val text = codeFieldValue.text
                            val cursor = codeFieldValue.selection.start
                            val lastWord = textBeforeCursor.substringAfterLast(' ').substringAfterLast('\n').substringAfterLast('\t').trimStart('<')
                            val replaceStart = (cursor - lastWord.length).coerceAtLeast(0)
                            val newText = text.replaceRange(replaceStart, cursor, item.insertText)
                            onCodeChange(codeFieldValue.copy(text = newText, selection = TextRange(replaceStart + item.cursorOffset)))
                        },
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(item.label, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            Text(item.detail, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        // Programming Symbol Bar (Keyboard Accessory Toolbar)
        ProgrammingSymbolBar(
            currentFieldValue = codeFieldValue,
            onValueChange = onCodeChange
        )

        // Bottom Status Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth().height(30.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Lines: $lineCount", style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Pos: ${codeFieldValue.selection.start}", style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text("${codeFieldValue.text.length} chars • UTF-8", style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }

    // Palette Bottom Sheet
    if (showPaletteSheet) {
        ComponentPaletteSheet(
            onInsertSnippet = onInsertSnippet,
            onDismiss = { showPaletteSheet = false }
        )
    }

    // CSS Generator Bottom Sheet
    if (showGeneratorsSheet) {
        GeneratorsSheet(
            onInsertCss = { css -> onInsertSnippet("", css) },
            onDismiss = { showGeneratorsSheet = false }
        )
    }

    // Design Tokens Sheet
    if (showDesignTokensSheet) {
        DesignTokensSheet(
            onApplyTokens = onApplyDesignTokens,
            onDismiss = { showDesignTokensSheet = false }
        )
    }

    // Color Picker Dialog
    activeColorToken?.let { token ->
        ColorPickerDialog(
            initialColorHex = token.text,
            onColorSelected = { newHex ->
                onReplaceColorToken(token.range, newHex)
                activeColorToken = null
            },
            onDismiss = { activeColorToken = null }
        )
    }

    // Performance & Accessibility Audit Sheet
    if (showAuditSheet) {
        val htmlContent = files.find { it.fileType == FileType.HTML }?.content ?: ""
        val cssContent = files.find { it.fileType == FileType.CSS }?.content ?: ""
        AuditSheet(
            htmlContent = htmlContent,
            cssContent = cssContent,
            onDismiss = { showAuditSheet = false }
        )
    }

    // Media & Asset Manager Sheet
    if (showAssetSheet) {
        AssetManagerSheet(
            files = files,
            onImportAsset = onImportAsset,
            onInsertAssetTag = { tag -> onInsertSnippet(tag, "") },
            onDismiss = { showAssetSheet = false }
        )
    }
}
