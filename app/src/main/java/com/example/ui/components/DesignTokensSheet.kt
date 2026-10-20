package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignTokensSheet(
    onApplyTokens: (cssVariables: String) -> Unit,
    onDismiss: () -> Unit
) {
    var primaryColor by remember { mutableStateOf("#6366F1") }
    var accentColor by remember { mutableStateOf("#22D3EE") }
    var borderRadius by remember { mutableIntStateOf(16) }
    var selectedFont by remember { mutableStateOf("Inter") }

    val fonts = listOf("Inter", "Poppins", "Manrope", "DM Sans", "Space Grotesk", "JetBrains Mono")
    val colorPresets = listOf("#6366F1", "#22D3EE", "#34D399", "#EC4899", "#F59E0B", "#8B5CF6")

    val generatedRootCss = """:root {
  --primary: $primaryColor;
  --accent: $accentColor;
  --radius: ${borderRadius}px;
  --font-family: '$selectedFont', -apple-system, BlinkMacSystemFont, sans-serif;
}
"""

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text("Design Tokens (:root)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }
                IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, contentDescription = "Close") }
            }

            // Primary Color Picker
            Column {
                Text("Primary Brand Color", style = MaterialTheme.typography.labelMedium)
                Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    colorPresets.forEach { hex ->
                        val c = try { Color(android.graphics.Color.parseColor(hex)) } catch (e: Exception) { Color.Gray }
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(c, CircleShape)
                                .border(if (primaryColor == hex) 3.dp else 1.dp, Color.White, CircleShape)
                                .clickable { primaryColor = hex }
                        )
                    }
                }
            }

            // Accent Color Picker
            Column {
                Text("Accent Gradient Color", style = MaterialTheme.typography.labelMedium)
                Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    colorPresets.reversed().forEach { hex ->
                        val c = try { Color(android.graphics.Color.parseColor(hex)) } catch (e: Exception) { Color.Gray }
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(c, CircleShape)
                                .border(if (accentColor == hex) 3.dp else 1.dp, Color.White, CircleShape)
                                .clickable { accentColor = hex }
                        )
                    }
                }
            }

            // Border Radius
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Border Radius", style = MaterialTheme.typography.labelMedium)
                    Text("${borderRadius}px", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
                Slider(value = borderRadius.toFloat(), onValueChange = { borderRadius = it.toInt() }, valueRange = 4f..32f)
            }

            // Font Family
            Column {
                Text("Font Family", style = MaterialTheme.typography.labelMedium)
                Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    fonts.take(3).forEach { font ->
                        FilterChip(
                            selected = selectedFont == font,
                            onClick = { selectedFont = font },
                            label = { Text(font, fontSize = 11.sp) }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    onApplyTokens(generatedRootCss)
                    onDismiss()
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().testTag("apply_tokens_btn")
            ) {
                Text("Apply Tokens to style.css")
            }
        }
    }
}
