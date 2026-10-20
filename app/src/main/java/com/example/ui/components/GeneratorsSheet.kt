package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratorsSheet(
    onInsertCss: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var activeTab by remember { mutableStateOf("Gradient") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text("CSS Style Generator", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Gradient", "Glass", "Shadow").forEach { tab ->
                    FilterChip(
                        selected = activeTab == tab,
                        onClick = { activeTab = tab },
                        label = { Text(tab) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            when (activeTab) {
                "Gradient" -> GradientGeneratorView(onInsert = { onInsertCss(it); onDismiss() })
                "Glass" -> GlassmorphismGeneratorView(onInsert = { onInsertCss(it); onDismiss() })
                "Shadow" -> ShadowGeneratorView(onInsert = { onInsertCss(it); onDismiss() })
            }
        }
    }
}

@Composable
private fun GradientGeneratorView(onInsert: (String) -> Unit) {
    var angle by remember { mutableIntStateOf(135) }
    var color1 by remember { mutableStateOf("#6366F1") }
    var color2 by remember { mutableStateOf("#22D3EE") }

    val css = "background: linear-gradient(${angle}deg, $color1 0%, $color2 100%);\n"

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Preview box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF6366F1), Color(0xFF22D3EE))
                    ),
                    RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("Preview", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Angle", style = MaterialTheme.typography.bodyMedium)
            Text("${angle}°", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = angle.toFloat(),
            onValueChange = { angle = it.toInt() },
            valueRange = 0f..360f
        )

        Button(
            onClick = { onInsert(css) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("insert_gradient_btn")
        ) {
            Text("Insert Gradient CSS")
        }
    }
}

@Composable
private fun GlassmorphismGeneratorView(onInsert: (String) -> Unit) {
    var blurVal by remember { mutableIntStateOf(20) }
    var opacityVal by remember { mutableFloatStateOf(0.12f) }

    val css = """background: rgba(255, 255, 255, ${String.format("%.2f", opacityVal)});\nbackdrop-filter: blur(${blurVal}px);\n-webkit-backdrop-filter: blur(${blurVal}px);\nborder: 1px solid rgba(255, 255, 255, 0.15);\nborder-radius: 20px;\n"""

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(
                    Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF0E1118))),
                    RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = Color.White.copy(alpha = opacityVal),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                modifier = Modifier.size(140.dp, 60.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Glass Card", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Blur Strength", style = MaterialTheme.typography.bodyMedium)
            Text("${blurVal}px", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }
        Slider(value = blurVal.toFloat(), onValueChange = { blurVal = it.toInt() }, valueRange = 4f..40f)

        Button(
            onClick = { onInsert(css) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("insert_glass_btn")
        ) {
            Text("Insert Glassmorphism CSS")
        }
    }
}

@Composable
private fun ShadowGeneratorView(onInsert: (String) -> Unit) {
    var blurRadius by remember { mutableIntStateOf(24) }
    var yOffset by remember { mutableIntStateOf(10) }

    val css = "box-shadow: 0 ${yOffset}px ${blurRadius}px rgba(0, 0, 0, 0.45);\n"

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(Color(0xFF0E1118), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = Color(0xFF151926),
                shape = RoundedCornerShape(14.dp),
                shadowElevation = blurRadius.dp,
                modifier = Modifier.size(120.dp, 50.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Shadow", color = Color.White, fontSize = 12.sp)
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Blur Radius", style = MaterialTheme.typography.bodyMedium)
            Text("${blurRadius}px", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }
        Slider(value = blurRadius.toFloat(), onValueChange = { blurRadius = it.toInt() }, valueRange = 4f..50f)

        Button(
            onClick = { onInsert(css) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Insert Box-Shadow CSS")
        }
    }
}
