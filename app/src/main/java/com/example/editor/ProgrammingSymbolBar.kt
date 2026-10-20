package com.example.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StudioCyan

sealed class SymbolItem(val label: String, val insertText: String, val cursorOffset: Int) {
    class PairSymbol(label: String, val open: String, val close: String) :
        SymbolItem(label, open + close, open.length)
    class SingleSymbol(label: String, insert: String = label) :
        SymbolItem(label, insert, insert.length)
    class TabSymbol(val spaces: Int = 2) :
        SymbolItem("TAB", " ".repeat(spaces), spaces)
    class EmmetSymbol :
        SymbolItem("⚡ Emmet", "", 0)
}

val DefaultProgrammingSymbols = listOf(
    SymbolItem.TabSymbol(2),
    SymbolItem.EmmetSymbol(),
    SymbolItem.PairSymbol("<>", "<", ">"),
    SymbolItem.SingleSymbol("<"),
    SymbolItem.SingleSymbol(">"),
    SymbolItem.SingleSymbol("/"),
    SymbolItem.SingleSymbol("="),
    SymbolItem.PairSymbol("{}", "{", "}"),
    SymbolItem.PairSymbol("()", "(", ")"),
    SymbolItem.PairSymbol("[]", "[", "]"),
    SymbolItem.PairSymbol("\"\"", "\"", "\""),
    SymbolItem.PairSymbol("''", "'", "'"),
    SymbolItem.SingleSymbol(";"),
    SymbolItem.SingleSymbol(":"),
    SymbolItem.SingleSymbol("."),
    SymbolItem.SingleSymbol(","),
    SymbolItem.SingleSymbol("!"),
    SymbolItem.SingleSymbol("+"),
    SymbolItem.SingleSymbol("-"),
    SymbolItem.SingleSymbol("*"),
    SymbolItem.SingleSymbol("&"),
    SymbolItem.SingleSymbol("|"),
    SymbolItem.SingleSymbol("$"),
    SymbolItem.SingleSymbol("#"),
    SymbolItem.SingleSymbol("?"),
    SymbolItem.SingleSymbol("_"),
    SymbolItem.SingleSymbol("\\")
)

@Composable
fun ProgrammingSymbolBar(
    currentFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    symbols: List<SymbolItem> = DefaultProgrammingSymbols
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .horizontalScroll(scrollState)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        symbols.forEach { symbol ->
            val isAccent = symbol is SymbolItem.EmmetSymbol || symbol is SymbolItem.TabSymbol
            Surface(
                onClick = {
                    val text = currentFieldValue.text
                    val selection = currentFieldValue.selection
                    val min = selection.min
                    val max = selection.max

                    if (symbol is SymbolItem.TabSymbol || symbol is SymbolItem.EmmetSymbol) {
                        // Check if there is an Emmet expression or shortcode before the cursor
                        val beforeCursor = if (min in 0..text.length) text.substring(0, min) else ""
                        val lastToken = beforeCursor.takeLastWhile { !it.isWhitespace() && it != '<' && it != '>' }

                        val expanded = if (lastToken.isNotEmpty()) EmmetEngine.expand(lastToken) else null
                        if (expanded != null) {
                            val tokenStart = min - lastToken.length
                            val newText = text.replaceRange(tokenStart, max, expanded)
                            val newCursorPos = tokenStart + expanded.length
                            onValueChange(
                                currentFieldValue.copy(
                                    text = newText,
                                    selection = TextRange(newCursorPos)
                                )
                            )
                            return@Surface
                        }

                        // If not an Emmet candidate and it's Emmet button, insert a helpful card snippet
                        if (symbol is SymbolItem.EmmetSymbol) {
                            val defaultSnippet = EmmetEngine.SHORTCODES["card"] ?: "<div></div>"
                            val newText = text.replaceRange(min, max, defaultSnippet)
                            onValueChange(
                                currentFieldValue.copy(
                                    text = newText,
                                    selection = TextRange(min + defaultSnippet.length)
                                )
                            )
                            return@Surface
                        }
                    }

                    val newText = text.replaceRange(min, max, symbol.insertText)
                    val newCursorPos = min + symbol.cursorOffset
                    onValueChange(
                        currentFieldValue.copy(
                            text = newText,
                            selection = TextRange(newCursorPos)
                        )
                    )
                },
                shape = RoundedCornerShape(8.dp),
                color = if (isAccent) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier
                    .widthIn(min = 34.dp)
                    .height(34.dp)
                    .testTag("symbol_btn_${symbol.label}")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = symbol.label,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = if (symbol.label.length > 3) 11.sp else 13.sp
                        ),
                        color = if (isAccent) StudioCyan else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
