package com.example.ui.preview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
import com.example.data.model.Project
import com.example.data.model.ProjectFile
import com.example.preview.ConsoleLogItem
import com.example.preview.DeviceFrame
import com.example.preview.DevicePreset
import com.example.preview.Orientation
import com.example.preview.PreviewBridge
import com.example.preview.PreviewHtmlGenerator
import com.example.preview.ScaleMode
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class InspectedElementInfo(
    val tagName: String,
    val selector: String,
    val padding: String,
    val margin: String,
    val fontSize: String,
    val color: String,
    val background: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    project: Project?,
    files: List<ProjectFile>,
    devicePreset: DevicePreset,
    orientation: Orientation,
    scaleMode: ScaleMode,
    safeAreaOverlay: Boolean,
    previewDarkMode: Boolean,
    consoleLogs: List<ConsoleLogItem>,
    onBackToEditor: () -> Unit,
    onSetDevicePreset: (DevicePreset) -> Unit,
    onToggleOrientation: () -> Unit,
    onSetScaleMode: (ScaleMode) -> Unit,
    onToggleSafeArea: () -> Unit,
    onTogglePreviewDarkMode: () -> Unit,
    onAddLog: (ConsoleLogItem) -> Unit,
    onClearLogs: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var reloadTrigger by remember { mutableStateOf(0) }
    var showDeviceMenu by remember { mutableStateOf(false) }
    var showConsoleSheet by remember { mutableStateOf(false) }
    var isInspectMode by remember { mutableStateOf(false) }
    var inspectedElement by remember { mutableStateOf<InspectedElementInfo?>(null) }
    var logFilter by remember { mutableStateOf("ALL") }
    var replCode by remember { mutableStateOf("") }

    val combinedHtml = remember(files, reloadTrigger, previewDarkMode) {
        val base = PreviewHtmlGenerator.generateCombinedHtml(files)
        // Inject dark/light mode preference override
        if (!previewDarkMode) {
            base.replace("<body>", "<body style=\"background-color:#ffffff !important; color:#0f172a !important;\">")
        } else base
    }

    val errorCount = remember(consoleLogs) {
        consoleLogs.count { it.level.equals("error", ignoreCase = true) }
    }

    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Toolbar
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = project?.name ?: "Preview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = "${devicePreset.label} • ${if (orientation == Orientation.PORTRAIT) "Portrait" else "Landscape"}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackToEditor, modifier = Modifier.testTag("preview_back_btn")) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                // Device Preset Selector
                Box {
                    IconButton(onClick = { showDeviceMenu = true }, modifier = Modifier.testTag("preview_device_btn")) {
                        Icon(imageVector = Icons.Default.Devices, contentDescription = "Device")
                    }
                    DropdownMenu(expanded = showDeviceMenu, onDismissRequest = { showDeviceMenu = false }) {
                        DevicePreset.entries.forEach { preset ->
                            DropdownMenuItem(
                                text = { Text(preset.label) },
                                onClick = {
                                    showDeviceMenu = false
                                    onSetDevicePreset(preset)
                                }
                            )
                        }
                    }
                }

                // Orientation Toggle
                IconButton(onClick = onToggleOrientation, modifier = Modifier.testTag("preview_rotate_btn")) {
                    Icon(imageVector = Icons.Default.ScreenRotation, contentDescription = "Rotate")
                }

                // Dark / Light Preview Toggle
                IconButton(onClick = onTogglePreviewDarkMode) {
                    Icon(
                        imageVector = if (previewDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Theme"
                    )
                }

                // Inspect Mode Toggle
                IconButton(
                    onClick = {
                        isInspectMode = !isInspectMode
                        if (isInspectMode) {
                            webViewRef?.evaluateJavascript(
                                """
                                (function() {
                                  window.__inspectActive = true;
                                  document.addEventListener('click', function(e) {
                                    if (!window.__inspectActive) return;
                                    e.preventDefault();
                                    e.stopPropagation();
                                    var el = e.target;
                                    var s = window.getComputedStyle(el);
                                    if (window.AndroidBridge && window.AndroidBridge.onInspect) {
                                      window.AndroidBridge.onInspect(
                                        el.tagName.toLowerCase(),
                                        el.className || '',
                                        s.padding || '0px',
                                        s.margin || '0px',
                                        s.fontSize || '14px',
                                        s.color || '#000000',
                                        s.backgroundColor || 'transparent'
                                      );
                                    }
                                  }, true);
                                })();
                                """.trimIndent(),
                                null
                            )
                            Toast.makeText(context, "Inspect Mode ON: Tap any element", Toast.LENGTH_SHORT).show()
                        } else {
                            webViewRef?.evaluateJavascript("window.__inspectActive = false;", null)
                            inspectedElement = null
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.TouchApp,
                        contentDescription = "Inspect",
                        tint = if (isInspectMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Scale Mode Cycle
                IconButton(
                    onClick = {
                        val nextMode = when (scaleMode) {
                            ScaleMode.FIT -> ScaleMode.ACTUAL
                            ScaleMode.ACTUAL -> ScaleMode.FULLSCREEN
                            ScaleMode.FULLSCREEN -> ScaleMode.FIT
                        }
                        onSetScaleMode(nextMode)
                    },
                    modifier = Modifier.testTag("preview_scale_btn")
                ) {
                    Icon(imageVector = Icons.Default.Fullscreen, contentDescription = "Scale")
                }

                // Reload Action
                IconButton(
                    onClick = {
                        reloadTrigger++
                        webViewRef?.reload()
                    },
                    modifier = Modifier.testTag("preview_reload_btn")
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reload")
                }

                // Console Drawer Toggle with Badge
                IconButton(
                    onClick = { showConsoleSheet = true },
                    modifier = Modifier.testTag("preview_console_btn")
                ) {
                    BadgedBox(
                        badge = {
                            if (errorCount > 0) {
                                Badge(containerColor = MaterialTheme.colorScheme.error) {
                                    Text(errorCount.toString())
                                }
                            } else if (consoleLogs.isNotEmpty()) {
                                Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                    Text(consoleLogs.size.toString())
                                }
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Terminal, contentDescription = "Console")
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        // Main Viewport Container
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFF07080C)),
            contentAlignment = Alignment.Center
        ) {
            val availableWidth = maxWidth
            val availableHeight = maxHeight

            val targetWidth = if (orientation == Orientation.PORTRAIT) devicePreset.baseWidth else devicePreset.baseHeight
            val targetHeight = if (orientation == Orientation.PORTRAIT) devicePreset.baseHeight else devicePreset.baseWidth

            val scaleFactor = if (scaleMode == ScaleMode.FIT && devicePreset != DevicePreset.RESPONSIVE) {
                val scaleW = (availableWidth.value - 32) / targetWidth.value
                val scaleH = (availableHeight.value - 32) / targetHeight.value
                minOf(1f, minOf(scaleW, scaleH))
            } else {
                1f
            }

            Box(
                modifier = Modifier
                    .scale(scaleFactor)
                    .padding(if (scaleMode == ScaleMode.FULLSCREEN) 0.dp else 16.dp),
                contentAlignment = Alignment.Center
            ) {
                DeviceFrame(
                    preset = devicePreset,
                    orientation = orientation,
                    scaleMode = scaleMode,
                    safeAreaOverlay = safeAreaOverlay
                ) {
                    @SuppressLint("SetJavaScriptEnabled")
                    AndroidView(
                        factory = { ctx ->
                            WebView(ctx).apply {
                                webViewRef = this
                                settings.apply {
                                    javaScriptEnabled = true
                                    domStorageEnabled = true
                                    allowFileAccess = false
                                    allowContentAccess = false
                                    useWideViewPort = true
                                    loadWithOverviewMode = true
                                    cacheMode = WebSettings.LOAD_NO_CACHE
                                }

                                addJavascriptInterface(
                                    object {
                                        @JavascriptInterface
                                        fun onConsoleMessage(level: String, message: String) {
                                            onAddLog(ConsoleLogItem(level = level, message = message))
                                        }

                                        @JavascriptInterface
                                        fun onError(message: String, line: Int, col: Int) {
                                            onAddLog(ConsoleLogItem(level = "error", message = "$message (Line $line:$col)", lineNumber = line))
                                        }

                                        @JavascriptInterface
                                        fun onInspect(tagName: String, className: String, padding: String, margin: String, fontSize: String, color: String, bg: String) {
                                            inspectedElement = InspectedElementInfo(
                                                tagName = tagName,
                                                selector = if (className.isNotBlank()) ".$className" else tagName,
                                                padding = padding,
                                                margin = margin,
                                                fontSize = fontSize,
                                                color = color,
                                                background = bg
                                            )
                                        }
                                    },
                                    "AndroidBridge"
                                )

                                webChromeClient = object : WebChromeClient() {
                                    override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                                        val level = when (consoleMessage.messageLevel()) {
                                            ConsoleMessage.MessageLevel.ERROR -> "error"
                                            ConsoleMessage.MessageLevel.WARNING -> "warn"
                                            else -> "log"
                                        }
                                        onAddLog(ConsoleLogItem(level = level, message = consoleMessage.message(), lineNumber = consoleMessage.lineNumber()))
                                        return true
                                    }
                                }

                                loadDataWithBaseURL("https://preview.local/", combinedHtml, "text/html", "UTF-8", null)
                            }
                        },
                        update = { webView ->
                            webViewRef = webView
                            webView.loadDataWithBaseURL("https://preview.local/", combinedHtml, "text/html", "UTF-8", null)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Tap-to-Inspect Bottom Panel
        inspectedElement?.let { elem ->
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(6.dp)) {
                                Text("<${elem.tagName}>", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                            Text(elem.selector, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { inspectedElement = null }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(16.dp))
                        }
                    }
                    Text(
                        text = "Padding: ${elem.padding} • Margin: ${elem.margin} • Font: ${elem.fontSize}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    // Console Logs Bottom Sheet
    if (showConsoleSheet) {
        val filteredLogs = remember(consoleLogs, logFilter) {
            when (logFilter) {
                "ERRORS" -> consoleLogs.filter { it.level.equals("error", ignoreCase = true) }
                "LOGS" -> consoleLogs.filter { it.level.equals("log", ignoreCase = true) }
                else -> consoleLogs
            }
        }

        ModalBottomSheet(
            onDismissRequest = { showConsoleSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Terminal, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text("JavaScript Console", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    }

                    Row {
                        IconButton(onClick = onClearLogs) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { showConsoleSheet = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = logFilter == "ALL", onClick = { logFilter = "ALL" }, label = { Text("All (${consoleLogs.size})") })
                    FilterChip(selected = logFilter == "ERRORS", onClick = { logFilter = "ERRORS" }, label = { Text("Errors ($errorCount)") })
                    FilterChip(selected = logFilter == "LOGS", onClick = { logFilter = "LOGS" }, label = { Text("Logs") })
                }

                if (filteredLogs.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().height(160.dp), contentAlignment = Alignment.Center) {
                        Text("No console messages or errors logged.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth().height(220.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(filteredLogs) { log ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(log.level.uppercase(Locale.ROOT), fontWeight = FontWeight.Bold, fontSize = 10.sp, color = if (log.level.equals("error", ignoreCase = true)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
                                    Column {
                                        Text(log.message, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                                        if (log.lineNumber > 0) {
                                            Text("Line ${log.lineNumber}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Interactive JavaScript REPL Input Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = replCode,
                        onValueChange = { replCode = it },
                        placeholder = { Text("eval JS: document.title, alert(1)...", fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("console_repl_input"),
                        shape = RoundedCornerShape(10.dp),
                        textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
                    )

                    Surface(
                        onClick = {
                            val expr = replCode.trim()
                            if (expr.isNotEmpty()) {
                                onAddLog(ConsoleLogItem(level = "log", message = "> $expr"))
                                webViewRef?.evaluateJavascript(expr) { result ->
                                    val cleanResult = if (result == "null" || result == null) "undefined" else result
                                    onAddLog(ConsoleLogItem(level = "log", message = "< $cleanResult"))
                                }
                                replCode = ""
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(46.dp).testTag("console_repl_run_btn")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Run JS", tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}
