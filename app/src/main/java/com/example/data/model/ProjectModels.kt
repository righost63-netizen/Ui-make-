package com.example.data.model

data class Project(
    val id: String,
    val name: String,
    val description: String = "",
    val templateType: String = "blank",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val thumbnailColor: Long = 0xFF6366F1,
    val isStarred: Boolean = false,
    val folder: String = "All",
    val files: List<ProjectFile> = emptyList()
)

data class ProjectFile(
    val id: String,
    val projectId: String,
    val fileName: String,
    val fileType: FileType,
    val content: String,
    val updatedAt: Long = System.currentTimeMillis()
)

data class ProjectSnapshot(
    val id: String,
    val projectId: String,
    val label: String,
    val timestamp: Long,
    val filesSummary: String = ""
)

data class CodeSnippet(
    val id: String,
    val prefix: String,
    val title: String,
    val category: String, // Buttons, Cards, Navbars, Inputs, Toggles, Modals, Bottom sheets, Charts, Avatars
    val htmlCode: String,
    val cssCode: String,
    val isCustom: Boolean = false
)

enum class FileType {
    HTML,
    CSS,
    JAVASCRIPT,
    ASSET,
    OTHER;

    companion object {
        fun fromFileName(name: String): FileType {
            return when {
                name.endsWith(".html", ignoreCase = true) || name.endsWith(".htm", ignoreCase = true) -> HTML
                name.endsWith(".css", ignoreCase = true) -> CSS
                name.endsWith(".js", ignoreCase = true) -> JAVASCRIPT
                name.endsWith(".png", ignoreCase = true) ||
                        name.endsWith(".jpg", ignoreCase = true) ||
                        name.endsWith(".svg", ignoreCase = true) -> ASSET
                else -> OTHER
            }
        }
    }
}
