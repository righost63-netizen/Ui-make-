package com.example

import com.example.editor.EmmetEngine
import com.example.ui.components.CodeAuditor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StudioLogicTest {

    @Test
    fun testShortcodeExpansion() {
        val expandedCard = EmmetEngine.expand("card")
        assertNotNull(expandedCard)
        assertTrue(expandedCard!!.contains("card"))

        val expandedBtn = EmmetEngine.expand("btn")
        assertNotNull(expandedBtn)
        assertTrue(expandedBtn!!.contains("button"))
    }

    @Test
    fun testEmmetExpressionParsing() {
        val expanded = EmmetEngine.expand("div.card>h3+p")
        assertNotNull(expanded)
        assertTrue(expanded!!.contains("<div class=\"card\">"))
        assertTrue(expanded.contains("<h3></h3>"))
        assertTrue(expanded.contains("<p></p>"))

        val multiplied = EmmetEngine.expand("ul>li*3")
        assertNotNull(multiplied)
        assertTrue(multiplied!!.contains("<ul>"))
        val liCount = Regex("<li></li>").findAll(multiplied).count()
        assertEquals(3, liCount)
    }

    @Test
    fun testCodeAuditor() {
        val goodHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Test Page</title>
            </head>
            <body>
                <img src="test.png" alt="Test image">
                <input type="text" placeholder="Your name">
            </body>
            </html>
        """.trimIndent()

        val goodCss = """
            * { box-sizing: border-box; -webkit-tap-highlight-color: transparent; }
        """.trimIndent()

        val results = CodeAuditor.runAudit(goodHtml, goodCss)
        val allPassed = results.all { it.isPassed }
        assertTrue(allPassed)
    }
}
