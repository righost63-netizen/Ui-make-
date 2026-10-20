package com.example.editor

import com.example.data.model.FileType
import com.example.data.templates.ComponentLibrary

data class SuggestionItem(
    val label: String,
    val insertText: String,
    val detail: String,
    val cursorOffset: Int = insertText.length
)

object AutocompleteManager {

    private val HTML_TAGS = listOf(
        "div" to "<div>\n  \n</div>",
        "button" to "<button class=\"btn\">Click</button>",
        "span" to "<span></span>",
        "p" to "<p></p>",
        "h1" to "<h1></h1>",
        "h2" to "<h2></h2>",
        "h3" to "<h3></h3>",
        "section" to "<section>\n  \n</section>",
        "header" to "<header>\n  \n</header>",
        "nav" to "<nav>\n  \n</nav>",
        "input" to "<input type=\"text\" placeholder=\"\" />",
        "ul" to "<ul>\n  <li></li>\n</ul>",
        "li" to "<li></li>",
        "a" to "<a href=\"#\"></a>",
        "form" to "<form onsubmit=\"return false;\">\n  \n</form>"
    )

    private val HTML_ATTRS = listOf(
        "class" to "class=\"\"",
        "id" to "id=\"\"",
        "style" to "style=\"\"",
        "placeholder" to "placeholder=\"\"",
        "onclick" to "onclick=\"\"",
        "type" to "type=\"text\"",
        "href" to "href=\"#\""
    )

    private val CSS_PROPS = listOf(
        "display: flex;" to "display: flex;",
        "display: grid;" to "display: grid;",
        "justify-content: center;" to "justify-content: center;",
        "align-items: center;" to "align-items: center;",
        "background: linear-gradient(135deg, #6366F1, #22D3EE);" to "background: linear-gradient(135deg, #6366F1, #22D3EE);",
        "backdrop-filter: blur(20px);" to "backdrop-filter: blur(20px);",
        "border-radius: 16px;" to "border-radius: 16px;",
        "box-shadow: 0 10px 30px rgba(0,0,0,0.5);" to "box-shadow: 0 10px 30px rgba(0,0,0,0.5);",
        "padding: 16px;" to "padding: 16px;",
        "margin: 0 auto;" to "margin: 0 auto;",
        "font-size: 14px;" to "font-size: 14px;",
        "font-weight: 600;" to "font-weight: 600;",
        "color: #EEF1F8;" to "color: #EEF1F8;",
        "transition: all 0.2s ease;" to "transition: all 0.2s ease;"
    )

    fun getSuggestions(textBeforeCursor: String, fileType: FileType): List<SuggestionItem> {
        val lastWord = textBeforeCursor.substringAfterLast(' ')
            .substringAfterLast('\n')
            .substringAfterLast('\t')
            .trimStart('<')

        if (lastWord.isBlank()) return emptyList()

        val results = mutableListOf<SuggestionItem>()

        // Check Emmet shortcodes first
        ComponentLibrary.DEFAULT_SNIPPETS.forEach { s ->
            if (s.prefix.startsWith(lastWord, ignoreCase = true) || s.title.contains(lastWord, ignoreCase = true)) {
                results.add(
                    SuggestionItem(
                        label = s.prefix,
                        insertText = s.htmlCode,
                        detail = "Snippet: ${s.title}"
                    )
                )
            }
        }

        when (fileType) {
            FileType.HTML -> {
                HTML_TAGS.filter { it.first.startsWith(lastWord, ignoreCase = true) }.forEach {
                    results.add(SuggestionItem(it.first, it.second, "HTML Tag"))
                }
                HTML_ATTRS.filter { it.first.startsWith(lastWord, ignoreCase = true) }.forEach {
                    results.add(SuggestionItem(it.first, it.second, "Attribute", it.second.length - 1))
                }
            }
            FileType.CSS -> {
                CSS_PROPS.filter { it.first.contains(lastWord, ignoreCase = true) }.forEach {
                    results.add(SuggestionItem(it.first.substringBefore(':'), it.second, "CSS Property"))
                }
            }
            FileType.JAVASCRIPT -> {
                listOf(
                    "addEventListener" to "addEventListener('click', (e) => {\n  \n});",
                    "console.log" to "console.log();",
                    "document.getElementById" to "document.getElementById('');",
                    "document.querySelectorAll" to "document.querySelectorAll('');",
                    "const" to "const  = ;",
                    "function" to "function () {\n  \n}"
                ).filter { it.first.contains(lastWord, ignoreCase = true) }.forEach {
                    results.add(SuggestionItem(it.first, it.second, "JavaScript"))
                }
            }
            else -> {}
        }

        return results.take(6)
    }
}
