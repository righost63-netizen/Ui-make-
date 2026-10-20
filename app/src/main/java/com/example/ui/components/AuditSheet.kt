package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioRose
import com.example.ui.theme.StudioSuccess

data class AuditItem(
    val title: String,
    val description: String,
    val category: String, // "Accessibility", "Performance", "Best Practices"
    val isPassed: Boolean,
    val severity: String = if (isPassed) "PASS" else "WARN" // "PASS", "WARN", "FAIL"
)

object CodeAuditor {
    fun runAudit(html: String, css: String): List<AuditItem> {
        val results = mutableListOf<AuditItem>()

        // 1. Viewport Meta
        val hasViewport = html.contains("<meta", ignoreCase = true) && html.contains("viewport", ignoreCase = true)
        results.add(
            AuditItem(
                title = "Mobile Viewport Meta Tag",
                description = if (hasViewport) "Configured properly for high-DPI mobile screens" else "Missing <meta name=\"viewport\"> tag. Mobile scaling will look broken.",
                category = "Performance",
                isPassed = hasViewport,
                severity = if (hasViewport) "PASS" else "FAIL"
            )
        )

        // 2. Document Title
        val hasTitle = html.contains("<title>", ignoreCase = true) && !html.contains("<title></title>", ignoreCase = true)
        results.add(
            AuditItem(
                title = "Page Title Definition",
                description = if (hasTitle) "Title is set in <head>" else "Missing meaningful <title> tag for document heading.",
                category = "Best Practices",
                isPassed = hasTitle
            )
        )

        // 3. Image Alt Attributes
        val imgTagRegex = Regex("""<img\b([^>]*)>""", RegexOption.IGNORE_CASE)
        val imgMatches = imgTagRegex.findAll(html).toList()
        val missingAltCount = imgMatches.count { !it.value.contains("alt=", ignoreCase = true) }
        val allImagesHaveAlt = imgMatches.isEmpty() || missingAltCount == 0
        results.add(
            AuditItem(
                title = "Image Alt Attributes",
                description = if (allImagesHaveAlt) "All ${imgMatches.size} <img> tags have alt descriptions" else "$missingAltCount image(s) missing alt=\"...\" for screen readers",
                category = "Accessibility",
                isPassed = allImagesHaveAlt,
                severity = if (allImagesHaveAlt) "PASS" else "FAIL"
            )
        )

        // 4. Input Labels
        val inputTagRegex = Regex("""<input\b([^>]*)>""", RegexOption.IGNORE_CASE)
        val inputMatches = inputTagRegex.findAll(html).toList()
        val missingAriaCount = inputMatches.count {
            !it.value.contains("aria-label", ignoreCase = true) &&
                    !it.value.contains("placeholder", ignoreCase = true) &&
                    !it.value.contains("id=", ignoreCase = true)
        }
        val inputsHaveLabels = inputMatches.isEmpty() || missingAriaCount == 0
        results.add(
            AuditItem(
                title = "Form Input Accessibility",
                description = if (inputsHaveLabels) "Form controls have accessible identifiers" else "$missingAriaCount inputs missing label/placeholder/aria-label",
                category = "Accessibility",
                isPassed = inputsHaveLabels
            )
        )

        // 5. UTF-8 Charset
        val hasCharset = html.contains("charset", ignoreCase = true)
        results.add(
            AuditItem(
                title = "UTF-8 Encoding Declaration",
                description = if (hasCharset) "<meta charset=\"UTF-8\"> is present" else "Specify UTF-8 to prevent character rendering anomalies.",
                category = "Best Practices",
                isPassed = hasCharset
            )
        )

        // 6. Tap Target & Tap Highlight in CSS
        val hasTapHighlight = css.contains("-webkit-tap-highlight-color", ignoreCase = true)
        results.add(
            AuditItem(
                title = "Mobile Tap Highlight Cleanup",
                description = if (hasTapHighlight) "Modern mobile tap highlight configured" else "Add -webkit-tap-highlight-color: transparent for smooth mobile feel",
                category = "Performance",
                isPassed = hasTapHighlight
            )
        )

        // 7. Box-sizing reset
        val hasBoxSizing = css.contains("box-sizing", ignoreCase = true)
        results.add(
            AuditItem(
                title = "Box-Sizing Reset",
                description = if (hasBoxSizing) "box-sizing: border-box applied" else "Add * { box-sizing: border-box; } to prevent layout overflow",
                category = "Best Practices",
                isPassed = hasBoxSizing
            )
        )

        return results
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditSheet(
    htmlContent: String,
    cssContent: String,
    onDismiss: () -> Unit
) {
    val auditItems = remember(htmlContent, cssContent) {
        CodeAuditor.runAudit(htmlContent, cssContent)
    }

    val passedCount = auditItems.count { it.isPassed }
    val totalCount = auditItems.size
    val score = if (totalCount > 0) ((passedCount.toFloat() / totalCount) * 100).toInt() else 100

    val scoreColor = when {
        score >= 85 -> StudioSuccess
        score >= 60 -> StudioAmber
        else -> StudioRose
    }

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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Performance & Accessibility Audit",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Score Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth().testTag("audit_score_card")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(scoreColor.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$score%",
                            color = scoreColor,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 16.sp
                        )
                    }

                    Column {
                        Text(
                            text = if (score >= 85) "Excellent Mobile Quality" else if (score >= 60) "Good • A few fixes needed" else "Needs Attention",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "$passedCount of $totalCount criteria passed offline",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Audit Check Items
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(auditItems) { item ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (item.isPassed) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = if (item.isPassed) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (item.isPassed) StudioSuccess else StudioRose,
                                modifier = Modifier.size(20.dp)
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = item.title,
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Surface(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = item.category,
                                            fontSize = 9.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = item.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
