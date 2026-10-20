package com.example.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.data.model.CodeSnippet
import com.example.data.model.FileType
import com.example.data.model.Project
import com.example.data.model.ProjectFile
import com.example.data.model.ProjectSnapshot

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val templateType: String,
    val createdAt: Long,
    val updatedAt: Long,
    val thumbnailColor: Long,
    val isStarred: Boolean = false,
    val folder: String = "All"
) {
    fun toDomain(files: List<ProjectFile> = emptyList()): Project = Project(
        id = id,
        name = name,
        description = description,
        templateType = templateType,
        createdAt = createdAt,
        updatedAt = updatedAt,
        thumbnailColor = thumbnailColor,
        isStarred = isStarred,
        folder = folder,
        files = files
    )

    companion object {
        fun fromDomain(project: Project): ProjectEntity = ProjectEntity(
            id = project.id,
            name = project.name,
            description = project.description,
            templateType = project.templateType,
            createdAt = project.createdAt,
            updatedAt = project.updatedAt,
            thumbnailColor = project.thumbnailColor,
            isStarred = project.isStarred,
            folder = project.folder
        )
    }
}

@Entity(
    tableName = "project_files",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["projectId", "fileName"], unique = true)
    ]
)
data class ProjectFileEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val fileName: String,
    val fileType: String,
    val content: String,
    val updatedAt: Long
) {
    fun toDomain(): ProjectFile = ProjectFile(
        id = id,
        projectId = projectId,
        fileName = fileName,
        fileType = try { FileType.valueOf(fileType) } catch (e: Exception) { FileType.fromFileName(fileName) },
        content = content,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(file: ProjectFile): ProjectFileEntity = ProjectFileEntity(
            id = file.id,
            projectId = file.projectId,
            fileName = file.fileName,
            fileType = file.fileType.name,
            content = file.content,
            updatedAt = file.updatedAt
        )
    }
}

@Entity(
    tableName = "project_snapshots",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["projectId", "timestamp"])
    ]
)
data class ProjectSnapshotEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val label: String,
    val timestamp: Long,
    val filesSummary: String = "",
    val filesJson: String = ""
) {
    fun toDomain(): ProjectSnapshot = ProjectSnapshot(
        id = id,
        projectId = projectId,
        label = label,
        timestamp = timestamp,
        filesSummary = filesSummary
    )
}

@Entity(tableName = "snippets")
data class SnippetEntity(
    @PrimaryKey val id: String,
    val prefix: String,
    val title: String,
    val category: String,
    val htmlCode: String,
    val cssCode: String,
    val isCustom: Boolean
) {
    fun toDomain(): CodeSnippet = CodeSnippet(
        id = id,
        prefix = prefix,
        title = title,
        category = category,
        htmlCode = htmlCode,
        cssCode = cssCode,
        isCustom = isCustom
    )

    companion object {
        fun fromDomain(s: CodeSnippet): SnippetEntity = SnippetEntity(
            id = s.id,
            prefix = s.prefix,
            title = s.title,
            category = s.category,
            htmlCode = s.htmlCode,
            cssCode = s.cssCode,
            isCustom = s.isCustom
        )
    }
}
