package com.example.editor

import androidx.compose.ui.text.TextRange
import java.util.Stack

data class HistoryItem(
    val text: String,
    val selection: TextRange
)

class UndoRedoManager(private val maxDepth: Int = 40) {
    private val undoStack = Stack<HistoryItem>()
    private val redoStack = Stack<HistoryItem>()
    private var lastRecordedText: String = ""

    fun recordChange(text: String, selection: TextRange) {
        if (text == lastRecordedText) return
        undoStack.push(HistoryItem(text, selection))
        if (undoStack.size > maxDepth) {
            undoStack.removeAt(0)
        }
        redoStack.clear()
        lastRecordedText = text
    }

    fun canUndo(): Boolean = undoStack.size > 1

    fun canRedo(): Boolean = redoStack.isNotEmpty()

    fun undo(): HistoryItem? {
        if (!canUndo()) return null
        val current = undoStack.pop()
        redoStack.push(current)
        val target = undoStack.peek()
        lastRecordedText = target.text
        return target
    }

    fun redo(): HistoryItem? {
        if (!canRedo()) return null
        val target = redoStack.pop()
        undoStack.push(target)
        lastRecordedText = target.text
        return target
    }

    fun reset(initialText: String, selection: TextRange = TextRange.Zero) {
        undoStack.clear()
        redoStack.clear()
        undoStack.push(HistoryItem(initialText, selection))
        lastRecordedText = initialText
    }
}

object EditorUtils {

    /**
     * Calculates auto-indent when Enter key is pressed.
     * Returns pair of (indentedText, newCursorPosition).
     */
    fun calculateAutoIndent(text: String, cursorIndex: Int, indentSpaces: Int = 2): Pair<String, Int> {
        val safeCursor = cursorIndex.coerceIn(0, text.length)
        val textBefore = text.substring(0, safeCursor)
        val textAfter = text.substring(safeCursor)

        val lastLine = textBefore.substringAfterLast('\n')
        val leadingSpaces = lastLine.takeWhile { it == ' ' || it == '\t' }

        val trimmedLastLine = lastLine.trimEnd()
        val extraIndent = if (trimmedLastLine.endsWith("{") ||
            trimmedLastLine.endsWith("(") ||
            trimmedLastLine.endsWith("[") ||
            (trimmedLastLine.endsWith(">") && !trimmedLastLine.contains("</"))
        ) {
            " ".repeat(indentSpaces)
        } else {
            ""
        }

        val indentToAdd = "\n" + leadingSpaces + extraIndent
        val newText = textBefore + indentToAdd + textAfter
        val newCursor = safeCursor + indentToAdd.length
        return Pair(newText, newCursor)
    }

    /**
     * Finds matching bracket index in text given cursor position
     */
    fun findMatchingBracket(text: String, cursorIndex: Int): Int? {
        if (cursorIndex < 0 || cursorIndex >= text.length) return null
        val char = text.getOrNull(cursorIndex) ?: return null

        val pairs = mapOf(
            '{' to '}',
            '(' to ')',
            '[' to ']',
            '<' to '>'
        )
        val reversePairs = mapOf(
            '}' to '{',
            ')' to '(',
            ']' to '[',
            '>' to '<'
        )

        if (pairs.containsKey(char)) {
            val target = pairs[char]!!
            var depth = 1
            for (i in (cursorIndex + 1) until text.length) {
                if (text[i] == char) depth++
                else if (text[i] == target) {
                    depth--
                    if (depth == 0) return i
                }
            }
        } else if (reversePairs.containsKey(char)) {
            val target = reversePairs[char]!!
            var depth = 1
            for (i in (cursorIndex - 1) downTo 0) {
                if (text[i] == char) depth++
                else if (text[i] == target) {
                    depth--
                    if (depth == 0) return i
                }
            }
        }
        return null
    }

    /**
     * Find all occurrences of query in text
     */
    fun searchMatches(text: String, query: String, matchCase: Boolean): List<IntRange> {
        if (query.isEmpty() || text.isEmpty()) return emptyList()
        val results = mutableListOf<IntRange>()
        var startIndex = 0
        while (startIndex < text.length) {
            val found = text.indexOf(query, startIndex, ignoreCase = !matchCase)
            if (found == -1) break
            results.add(found until (found + query.length))
            startIndex = found + query.length
        }
        return results
    }
}
