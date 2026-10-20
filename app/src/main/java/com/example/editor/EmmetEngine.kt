package com.example.editor

import com.example.data.templates.ComponentLibrary

object EmmetEngine {

    /**
     * Map of common shortcodes to their HTML structure
     */
    val SHORTCODES = mapOf(
        "card" to """<div class="card">
  <h3>Card Title</h3>
  <p>Card description and content goes here.</p>
</div>""",
        "btn" to """<button class="btn btn-primary">Click Me</button>""",
        "nav" to """<nav class="navbar">
  <div class="brand">Logo</div>
  <ul class="nav-links">
    <li><a href="#">Home</a></li>
    <li><a href="#">About</a></li>
    <li><a href="#">Contact</a></li>
  </ul>
</nav>""",
        "glass" to """<div class="glass-card">
  <h2>Glassmorphism</h2>
  <p>Translucent frosted glass UI surface.</p>
</div>""",
        "hero" to """<section class="hero-section">
  <h1 class="hero-title">Build Stunning Interfaces</h1>
  <p class="hero-subtitle">Mobile-first prototypes in pure HTML, CSS, and JavaScript.</p>
  <button class="btn btn-primary">Get Started</button>
</section>""",
        "login" to """<div class="auth-card">
  <h2>Welcome Back</h2>
  <div class="input-group">
    <label>Email</label>
    <input type="email" placeholder="name@domain.com" />
  </div>
  <div class="input-group">
    <label>Password</label>
    <input type="password" placeholder="••••••••" />
  </div>
  <button class="btn btn-primary">Sign In</button>
</div>""",
        "input" to """<div class="input-group">
  <label>Label</label>
  <input type="text" placeholder="Enter value..." />
</div>""",
        "avatar" to """<div class="avatar-circle">
  <span>JD</span>
</div>""",
        "badge" to """<span class="badge badge-success">Active</span>""",
        "modal" to """<div class="modal-backdrop">
  <div class="modal-card">
    <h3>Modal Title</h3>
    <p>Modal content and dialog details.</p>
    <div class="modal-actions">
      <button class="btn btn-secondary">Cancel</button>
      <button class="btn btn-primary">Confirm</button>
    </div>
  </div>
</div>"""
    )

    /**
     * Expands a token if it matches a shortcode or an Emmet expression.
     * Returns expanded HTML string, or null if token is not an expandable expression.
     */
    fun expand(token: String): String? {
        val trimmed = token.trim()
        if (trimmed.isEmpty()) return null

        // 1. Direct Shortcode match
        SHORTCODES[trimmed.lowercase()]?.let { return it }

        // 2. ComponentLibrary snippet prefix match
        ComponentLibrary.DEFAULT_SNIPPETS.find { it.prefix.equals(trimmed, ignoreCase = true) }?.let {
            return it.htmlCode
        }

        // 3. Emmet Expression Parsing (e.g. div.card>h3+p, ul>li*4, etc.)
        if (isEmmetCandidate(trimmed)) {
            return try {
                parseEmmetExpression(trimmed)
            } catch (e: Exception) {
                null
            }
        }

        return null
    }

    private fun isEmmetCandidate(expr: String): Boolean {
        return expr.contains('>') || expr.contains('+') || expr.contains('*') ||
                expr.contains('.') || expr.contains('#')
    }

    private fun parseEmmetExpression(expr: String): String {
        // Handle child hierarchy with '>'
        val levels = expr.split(">").map { it.trim() }.filter { it.isNotEmpty() }
        if (levels.isEmpty()) return ""

        return buildHierarchy(levels, 0, indent = "  ")
    }

    private fun buildHierarchy(levels: List<String>, levelIndex: Int, indent: String): String {
        if (levelIndex >= levels.size) return ""
        val currentLevel = levels[levelIndex]
        val isDeepest = levelIndex == levels.size - 1

        // Handle sibling '+' at current level
        val siblings = currentLevel.split("+").map { it.trim() }.filter { it.isNotEmpty() }

        val sb = StringBuilder()
        for ((idx, sib) in siblings.withIndex()) {
            val (tagInfo, count) = parseMultipliedTag(sib)
            for (c in 1..count) {
                if (isDeepest) {
                    sb.append(tagInfo.toOpeningTag())
                    sb.append(tagInfo.toClosingTag())
                } else {
                    sb.append(tagInfo.toOpeningTag()).append("\n")
                    val childContent = buildHierarchy(levels, levelIndex + 1, indent + "  ")
                    childContent.lines().forEach { line ->
                        if (line.isNotEmpty()) {
                            sb.append(indent).append(line).append("\n")
                        }
                    }
                    sb.append(tagInfo.toClosingTag())
                }
                if (c < count || idx < siblings.size - 1) {
                    sb.append("\n")
                }
            }
        }
        return sb.toString()
    }

    private data class TagInfo(val tag: String, val id: String? = null, val classes: List<String> = emptyList()) {
        fun toOpeningTag(): String {
            val sb = StringBuilder("<$tag")
            if (!id.isNullOrBlank()) {
                sb.append(" id=\"$id\"")
            }
            if (classes.isNotEmpty()) {
                sb.append(" class=\"${classes.joinToString(" ")}\"")
            }
            sb.append(">")
            return sb.toString()
        }

        fun toClosingTag(): String = "</$tag>"
    }

    private fun parseMultipliedTag(segment: String): Pair<TagInfo, Int> {
        val parts = segment.split("*")
        val tagStr = parts[0]
        val count = if (parts.size > 1) parts[1].toIntOrNull()?.coerceIn(1, 20) ?: 1 else 1

        val tagInfo = parseTagWithClassesAndId(tagStr)
        return Pair(tagInfo, count)
    }

    private fun parseTagWithClassesAndId(str: String): TagInfo {
        var remaining = str
        var id: String? = null
        val classes = mutableListOf<String>()

        // Check for id
        if (remaining.contains("#")) {
            val idAndAfter = remaining.substringAfter("#")
            remaining = remaining.substringBefore("#")
            if (idAndAfter.contains(".")) {
                id = idAndAfter.substringBefore(".")
                classes.addAll(idAndAfter.substringAfter(".").split(".").filter { it.isNotEmpty() })
            } else {
                id = idAndAfter
            }
        }

        // Check for classes
        if (remaining.contains(".")) {
            val classParts = remaining.split(".")
            remaining = classParts[0]
            classes.addAll(classParts.drop(1).filter { it.isNotEmpty() })
        }

        val tag = if (remaining.isBlank()) "div" else remaining
        return TagInfo(tag = tag, id = id, classes = classes)
    }
}
