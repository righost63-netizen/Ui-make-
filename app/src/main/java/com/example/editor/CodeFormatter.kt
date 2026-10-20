package com.example.editor

import com.example.data.model.FileType

object CodeFormatter {

    fun format(content: String, fileType: FileType, indentSpaces: Int = 2): String {
        if (content.isBlank()) return content
        val indent = " ".repeat(indentSpaces)
        val lines = content.lines()
        val result = StringBuilder()
        var currentIndent = 0

        for (rawLine in lines) {
            val line = rawLine.trim()
            if (line.isEmpty()) {
                result.append("\n")
                continue
            }

            // Decrement indent for closing tags/brackets
            val isClosing = when (fileType) {
                FileType.HTML -> line.startsWith("</") || line.startsWith("-->")
                FileType.CSS, FileType.JAVASCRIPT -> line.startsWith("}") || line.startsWith("]") || line.startsWith(")")
                else -> false
            }

            if (isClosing && currentIndent > 0) {
                currentIndent--
            }

            result.append(indent.repeat(currentIndent)).append(line).append("\n")

            // Increment indent for opening tags/brackets
            val opensBlock = when (fileType) {
                FileType.HTML -> (line.startsWith("<") && !line.startsWith("</") && !line.endsWith("/>") && !line.startsWith("<!") && !line.contains("</"))
                FileType.CSS, FileType.JAVASCRIPT -> line.endsWith("{") || line.endsWith("(") || line.endsWith("[")
                else -> false
            }

            if (opensBlock) {
                currentIndent++
            }
        }

        return result.toString().trimEnd() + "\n"
    }
}
