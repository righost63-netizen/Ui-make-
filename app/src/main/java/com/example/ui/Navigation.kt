package com.example.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(val title: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    EDITOR("Editor", Icons.Default.Code),
    PROJECTS("Projects", Icons.Default.Folder),
    SETTINGS("Settings", Icons.Default.Settings),
    PREVIEW("Preview", Icons.Default.Code);

    companion object {
        val bottomNavScreens = listOf(HOME, EDITOR, PROJECTS, SETTINGS)
    }
}
