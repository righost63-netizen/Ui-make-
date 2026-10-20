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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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

@Composable
fun ColorPickerDialog(
    initialColorHex: String,
    onColorSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var hexInput by remember { mutableStateOf(initialColorHex) }
    var red by remember { mutableFloatStateOf(0.39f) }
    var green by remember { mutableFloatStateOf(0.40f) }
    var blue by remember { mutableFloatStateOf(0.95f) }

    val currentColor = Color(red, green, blue)

    val presetSwatches = listOf(
        "#6366F1", "#22D3EE", "#34D399", "#FBBF24", "#F43F5E",
        "#A855F7", "#EC4899", "#06B6D4", "#10B981", "#E11D48",
        "#07080C", "#0E1118", "#151926", "#EEF1F8", "#FFFFFF"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Color Picker", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Color Preview Box
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(currentColor, RoundedCornerShape(14.dp))
                            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                    )

                    OutlinedTextField(
                        value = hexInput,
                        onValueChange = {
                            hexInput = it
                            try {
                                val c = android.graphics.Color.parseColor(it)
                                red = android.graphics.Color.red(c) / 255f
                                green = android.graphics.Color.green(c) / 255f
                                blue = android.graphics.Color.blue(c) / 255f
                            } catch (e: Exception) {}
                        },
                        label = { Text("Hex Code") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Preset Swatches
                Text("Palette Swatches", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presetSwatches.take(7).forEach { swatch ->
                        val color = try { Color(android.graphics.Color.parseColor(swatch)) } catch (e: Exception) { Color.Gray }
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(color, CircleShape)
                                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                                .clickable {
                                    hexInput = swatch
                                    red = color.red
                                    green = color.green
                                    blue = color.blue
                                }
                        )
                    }
                }

                // RGB Sliders
                Column {
                    Text("Red (${(red * 255).toInt()})", style = MaterialTheme.typography.labelSmall)
                    Slider(value = red, onValueChange = {
                        red = it
                        hexInput = String.format("#%02X%02X%02X", (red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt())
                    })
                    Text("Green (${(green * 255).toInt()})", style = MaterialTheme.typography.labelSmall)
                    Slider(value = green, onValueChange = {
                        green = it
                        hexInput = String.format("#%02X%02X%02X", (red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt())
                    })
                    Text("Blue (${(blue * 255).toInt()})", style = MaterialTheme.typography.labelSmall)
                    Slider(value = blue, onValueChange = {
                        blue = it
                        hexInput = String.format("#%02X%02X%02X", (red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt())
                    })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onColorSelected(hexInput); onDismiss() },
                modifier = Modifier.testTag("apply_color_btn")
            ) {
                Text("Apply Color")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
