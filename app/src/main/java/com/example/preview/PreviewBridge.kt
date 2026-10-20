package com.example.preview

import android.webkit.JavascriptInterface
import com.example.data.model.FileType
import com.example.data.model.ProjectFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

data class ConsoleLogItem(
    val id: String = UUID.randomUUID().toString(),
    val level: String, // "log", "warn", "error"
    val message: String,
    val lineNumber: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

class PreviewBridge(
    private val onLogReceived: (ConsoleLogItem) -> Unit
) {
    @JavascriptInterface
    fun onConsoleMessage(level: String, message: String) {
        val item = ConsoleLogItem(
            level = level,
            message = message
        )
        onLogReceived(item)
    }

    @JavascriptInterface
    fun onError(message: String, line: Int, col: Int) {
        val item = ConsoleLogItem(
            level = "error",
            message = "$message (Line $line:$col)",
            lineNumber = line
        )
        onLogReceived(item)
    }
}

object PreviewHtmlGenerator {

    /**
     * Combines HTML, CSS, and JS files into an isolated, self-contained HTML document
     * with an injected console bridge and safe error handling.
     */
    fun generateCombinedHtml(files: List<ProjectFile>): String {
        val htmlFile = files.find { it.fileName.equals("index.html", ignoreCase = true) }
            ?: files.find { it.fileType == FileType.HTML }
        val cssFiles = files.filter { it.fileName.endsWith(".css", ignoreCase = true) }
        val jsFiles = files.filter { it.fileName.endsWith(".js", ignoreCase = true) }

        var rawHtml = htmlFile?.content ?: """<!DOCTYPE html>
<html><head><meta name="viewport" content="width=device-width, initial-scale=1.0"></head>
<body><h1 style="color:#888;text-align:center;margin-top:40px;">No index.html found</h1></body></html>"""

        // Combine CSS
        val combinedCss = buildString {
            append("\n/* Injected Styles */\n")
            for (css in cssFiles) {
                append("\n/* --- ${css.fileName} --- */\n")
                append(css.content)
            }
        }

        // Bridge script to capture logs and errors
        val bridgeScript = """
<script>
(function() {
  var _origLog = console.log;
  var _origWarn = console.warn;
  var _origError = console.error;
  
  console.log = function() {
    var args = Array.prototype.slice.call(arguments);
    _origLog.apply(console, args);
    try {
      var msg = args.map(function(a) { 
        return (typeof a === 'object') ? JSON.stringify(a) : String(a); 
      }).join(' ');
      if (window.AndroidBridge && window.AndroidBridge.onConsoleMessage) {
        window.AndroidBridge.onConsoleMessage('log', msg);
      }
    } catch(e) {}
  };

  console.warn = function() {
    var args = Array.prototype.slice.call(arguments);
    _origWarn.apply(console, args);
    try {
      var msg = args.map(function(a) { 
        return (typeof a === 'object') ? JSON.stringify(a) : String(a); 
      }).join(' ');
      if (window.AndroidBridge && window.AndroidBridge.onConsoleMessage) {
        window.AndroidBridge.onConsoleMessage('warn', msg);
      }
    } catch(e) {}
  };

  console.error = function() {
    var args = Array.prototype.slice.call(arguments);
    _origError.apply(console, args);
    try {
      var msg = args.map(function(a) { 
        return (typeof a === 'object') ? JSON.stringify(a) : String(a); 
      }).join(' ');
      if (window.AndroidBridge && window.AndroidBridge.onConsoleMessage) {
        window.AndroidBridge.onConsoleMessage('error', msg);
      }
    } catch(e) {}
  };

  window.onerror = function(msg, url, line, col, error) {
    try {
      if (window.AndroidBridge && window.AndroidBridge.onError) {
        window.AndroidBridge.onError(String(msg), Number(line) || 0, Number(col) || 0);
      }
    } catch(e) {}
    return false;
  };
})();
</script>
"""

        // Combine JS
        val combinedJs = buildString {
            append(bridgeScript)
            append("\n<script>\n")
            for (js in jsFiles) {
                append("\n// --- ${js.fileName} ---\n")
                append(js.content)
                append("\n")
            }
            append("</script>\n")
        }

        // Insert CSS in <head> or at start
        val styleTag = "<style>$combinedCss</style>"
        val withCss = if (rawHtml.contains("</head>", ignoreCase = true)) {
            rawHtml.replace("</head>", "$styleTag\n</head>", ignoreCase = true)
        } else {
            styleTag + rawHtml
        }

        // Insert JS before </body> or at end
        val finalHtml = if (withCss.contains("</body>", ignoreCase = true)) {
            withCss.replace("</body>", "$combinedJs\n</body>", ignoreCase = true)
        } else {
            withCss + combinedJs
        }

        return finalHtml
    }
}
