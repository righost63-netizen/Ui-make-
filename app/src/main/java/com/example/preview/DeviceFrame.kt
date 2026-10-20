package com.example.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class DevicePreset(val label: String, val baseWidth: Dp, val baseHeight: Dp) {
    SMALL_PHONE("Small (360x640)", 360.dp, 640.dp),
    STANDARD("Standard (390x844)", 390.dp, 844.dp),
    PRO_MAX("Pro Max (430x932)", 430.dp, 932.dp),
    TABLET("Tablet (820x1180)", 820.dp, 1180.dp),
    RESPONSIVE("Responsive", Dp.Unspecified, Dp.Unspecified)
}

enum class Orientation {
    PORTRAIT,
    LANDSCAPE;

    fun toggle(): Orientation = if (this == PORTRAIT) LANDSCAPE else PORTRAIT
}

enum class ScaleMode(val label: String) {
    FIT("Fit"),
    ACTUAL("100%"),
    FULLSCREEN("Full")
}

@Composable
fun DeviceFrame(
    preset: DevicePreset,
    orientation: Orientation,
    scaleMode: ScaleMode,
    safeAreaOverlay: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (scaleMode == ScaleMode.FULLSCREEN || preset == DevicePreset.RESPONSIVE) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(0.dp))
        ) {
            content()
        }
        return
    }

    val rawWidth = if (orientation == Orientation.PORTRAIT) preset.baseWidth else preset.baseHeight
    val rawHeight = if (orientation == Orientation.PORTRAIT) preset.baseHeight else preset.baseWidth
    val cornerRadius = when (preset) {
        DevicePreset.PRO_MAX, DevicePreset.STANDARD -> 44.dp
        DevicePreset.SMALL_PHONE -> 28.dp
        DevicePreset.TABLET -> 24.dp
        else -> 32.dp
    }

    Box(
        modifier = modifier
            .width(rawWidth)
            .height(rawHeight)
            .shadow(32.dp, RoundedCornerShape(cornerRadius), ambientColor = Color(0xFF6366F1).copy(alpha = 0.2f))
            .border(3.5.dp, Color(0xFF283145), RoundedCornerShape(cornerRadius))
            .background(Color.Black, RoundedCornerShape(cornerRadius))
            .clip(RoundedCornerShape(cornerRadius))
    ) {
        // WebView Content
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }

        // Safe Area Overlay / Notch
        if (safeAreaOverlay && orientation == Orientation.PORTRAIT) {
            when (preset) {
                DevicePreset.PRO_MAX, DevicePreset.STANDARD -> {
                    // Dynamic Island pill
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 11.dp)
                            .width(116.dp)
                            .height(28.dp)
                            .background(Color.Black, RoundedCornerShape(20.dp))
                    )
                    // Home indicator bar at bottom
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                            .width(134.dp)
                            .height(5.dp)
                            .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(3.dp))
                    )
                }
                DevicePreset.SMALL_PHONE -> {
                    // Punch hole camera
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 10.dp)
                            .size(12.dp)
                            .background(Color(0xFF1E2430), CircleShape)
                            .border(1.dp, Color(0xFF333D52), CircleShape)
                    )
                }
                else -> {}
            }
        }
    }
}
