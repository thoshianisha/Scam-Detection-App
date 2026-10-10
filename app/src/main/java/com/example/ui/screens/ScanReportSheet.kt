package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.ScanReport
import com.example.model.ScannerType
import com.example.model.ThreatStatus
import com.example.model.VideoTimelineMarker
import com.example.ui.components.AnimatedCircularRiskGauge
import com.example.ui.components.GlassmorphicCyberCard
import com.example.ui.components.StatusPillBadge
import com.example.ui.components.statusColor
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SafeEmerald
import com.example.ui.theme.SuspiciousAmber
import com.example.ui.theme.ThreatCrimson

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScanReportDetailOverlay(
    report: ScanReport,
    onDismiss: () -> Unit,
    onReplayVoice: (ScanReport) -> Unit,
    onAskShieldAi: (ScanReport) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onDismiss() }

    val accent = statusColor(report.status)
    val infiniteTransition = rememberInfiniteTransition(label = "result_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.78f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("scan_report_detail_overlay"),
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.98f)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top bar with Close & Voice Replay
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = report.scannerType.emoji,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "FORENSIC SCAN REPORT",
                                style = MaterialTheme.typography.labelMedium,
                                color = CyberCyan
                            )
                            Text(
                                text = "${report.scannerType.title} • ${report.timestamp}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = { onReplayVoice(report) },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            border = BorderStroke(1.dp, accent.copy(alpha = 0.6f)),
                            modifier = Modifier.testTag("replay_voice_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.RecordVoiceOver,
                                contentDescription = "Replay Voice Alert",
                                tint = accent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Voice Alert", style = MaterialTheme.typography.labelMedium, color = accent)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("close_report_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close Report"
                            )
                        }
                    }
                }
            }

            // Animated Glowing Verdict Banner & Risk Gauge
            item {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = accent.copy(alpha = 0.12f),
                    border = BorderStroke(2.dp, accent.copy(alpha = glowAlpha)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        StatusPillBadge(
                            status = report.status,
                            customLabel = when (report.status) {
                                ThreatStatus.HIGH_RISK -> "🔴 THREAT DETECTED — HIGH RISK"
                                ThreatStatus.SUSPICIOUS -> "🟡 SUSPICIOUS CONTENT DETECTED"
                                ThreatStatus.SAFE -> "🟢 VERIFIED SAFE CONTENT"
                            }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AnimatedCircularRiskGauge(
                                score = report.riskScore,
                                label = "RISK SCORE",
                                status = report.status,
                                size = 132.dp
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = report.threatCategory,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = accent
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = report.headlineSummary,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Target: ${report.targetInput}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 3
                                )
                            }
                        }
                    }
                }
            }

            // Specialized Visual Meter for Image Authenticity Scanner
            if (report.scannerType == ScannerType.IMAGE_AUTH && report.authenticityScore != null) {
                item {
                    ImageAuthenticityMeterCard(
                        authenticityScore = report.authenticityScore,
                        aiGeneratedProb = report.aiGeneratedProbability ?: 0,
                        manipulationProb = report.manipulationProbability ?: 0
                    )
                }
            }

            // Specialized Timeline Visual Indicator for Video Authenticity Scanner
            if (report.scannerType == ScannerType.VIDEO_AUTH) {
                item {
                    VideoDeepfakeTimelineCard(
                        deepfakeProb = report.deepfakeProbability ?: report.riskScore,
                        confidenceLevel = report.confidenceLevel ?: "High (90%)",
                        markers = report.videoTimelineMarkers
                    )
                }
            }

            // Telemetry & Metadata Grid
            if (report.metadataItems.isNotEmpty()) {
                item {
                    GlassmorphicCyberCard(accentColor = CyberCyan) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Forensic Telemetry & Metadata",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                report.metadataItems.forEach { item ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = item.key,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = item.value,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (item.isFlagged) accent else SafeEmerald,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Explainable AI ("WHY the system reached this result")
            item {
                GlassmorphicCyberCard(accentColor = NeonPurple) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = "Explainable AI",
                                tint = NeonPurple,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Explainable AI — Why This Verdict Was Reached",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = NeonPurple
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = report.aiExplanation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Key Detection Evidence:",
                            style = MaterialTheme.typography.labelLarge,
                            color = CyberCyan
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            report.detectionReasons.forEach { reason ->
                                Row(verticalAlignment = Alignment.Top) {
                                    Text(
                                        text = "•",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = accent,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = reason,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Highlighted Suspicious Phrases / Flagged Permissions / PDF Sections
            if (report.suspiciousPhrases.isNotEmpty() || report.flaggedPermissions.isNotEmpty() || report.highlightedSections.isNotEmpty()) {
                item {
                    GlassmorphicCyberCard(accentColor = accent) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (report.suspiciousPhrases.isNotEmpty()) {
                                Text(
                                    text = "Highlighted Suspicious Triggers",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    report.suspiciousPhrases.forEach { phrase ->
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = ThreatCrimson.copy(alpha = 0.18f),
                                            border = BorderStroke(1.dp, ThreatCrimson.copy(alpha = 0.5f))
                                        ) {
                                            Text(
                                                text = "⚠ \"$phrase\"",
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                                style = MaterialTheme.typography.labelMedium,
                                                color = ThreatCrimson
                                            )
                                        }
                                    }
                                }
                            }

                            if (report.flaggedPermissions.isNotEmpty()) {
                                Text(
                                    text = "Flagged Dangerous Android Permissions",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    report.flaggedPermissions.forEach { perm ->
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = ThreatCrimson.copy(alpha = 0.14f),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "🔒 $perm",
                                                modifier = Modifier.padding(10.dp),
                                                style = MaterialTheme.typography.labelMedium,
                                                color = ThreatCrimson
                                            )
                                        }
                                    }
                                }
                            }

                            if (report.highlightedSections.isNotEmpty()) {
                                Text(
                                    text = "Highlighted Document Excerpts",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    report.highlightedSections.forEach { section ->
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = SuspiciousAmber.copy(alpha = 0.14f),
                                            border = BorderStroke(1.dp, SuspiciousAmber.copy(alpha = 0.4f)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = section,
                                                modifier = Modifier.padding(10.dp),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Smart Recommendations Card
            item {
                GlassmorphicCyberCard(accentColor = SafeEmerald) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Shield,
                                contentDescription = null,
                                tint = SafeEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Smart Security Recommendations",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = SafeEmerald
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            report.recommendedActions.forEach { action ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = null,
                                        tint = SafeEmerald,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = action,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Action Buttons: Ask Shield AI & Done
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { onAskShieldAi(report) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("ask_shield_ai_report_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                    ) {
                        Icon(imageVector = Icons.Filled.SmartToy, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ask Shield AI", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, CyberCyan)
                    ) {
                        Text("Done", color = CyberCyan, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ImageAuthenticityMeterCard(
    authenticityScore: Int,
    aiGeneratedProb: Int,
    manipulationProb: Int
) {
    GlassmorphicCyberCard(accentColor = ElectricBlue) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Visual Authenticity Meter",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            MeterProgressRow(
                label = "Overall Image Authenticity",
                percent = authenticityScore,
                color = if (authenticityScore >= 70) SafeEmerald else if (authenticityScore >= 40) SuspiciousAmber else ThreatCrimson
            )
            Spacer(modifier = Modifier.height(8.dp))
            MeterProgressRow(
                label = "AI-Generated / Diffusion Probability",
                percent = aiGeneratedProb,
                color = if (aiGeneratedProb >= 50) ThreatCrimson else SafeEmerald
            )
            Spacer(modifier = Modifier.height(8.dp))
            MeterProgressRow(
                label = "Pixel Manipulation / Editing Probability",
                percent = manipulationProb,
                color = if (manipulationProb >= 50) SuspiciousAmber else SafeEmerald
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    tint = CyberCyan,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AI-based forensic assessment — not absolute proof.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun MeterProgressRow(
    label: String,
    percent: Int,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodySmall)
            Text(
                text = "$percent%",
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { (percent / 100f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
private fun VideoDeepfakeTimelineCard(
    deepfakeProb: Int,
    confidenceLevel: String,
    markers: List<VideoTimelineMarker>
) {
    GlassmorphicCyberCard(accentColor = ThreatCrimson) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Suspicious Frame Timeline Analysis",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Confidence: $confidenceLevel",
                    style = MaterialTheme.typography.labelSmall,
                    color = CyberCyan
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Visual Timeline Bar with anomalous frame segments highlighted in red/amber
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SafeEmerald.copy(alpha = 0.25f))
            ) {
                markers.forEach { marker ->
                    val startWeight = marker.startFraction.coerceIn(0.01f, 0.9f)
                    val widthFraction = (marker.endFraction - marker.startFraction).coerceIn(0.08f, 0.5f)
                    Row(modifier = Modifier.fillMaxSize()) {
                        Spacer(modifier = Modifier.weight(startWeight))
                        Box(
                            modifier = Modifier
                                .weight(widthFraction)
                                .fillMaxHeight()
                                .background(if (marker.isHighRisk) ThreatCrimson else SuspiciousAmber)
                        )
                        Spacer(modifier = Modifier.weight((1f - startWeight - widthFraction).coerceAtLeast(0.01f)))
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("00:00", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("00:12", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("00:25", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            if (markers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    markers.forEach { marker ->
                        val c = if (marker.isHighRisk) ThreatCrimson else SuspiciousAmber
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = c.copy(alpha = 0.14f),
                            border = BorderStroke(1.dp, c.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "⏱ ${marker.timestampRange}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = c,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = marker.anomalyDescription,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Text(
                                    text = "${marker.confidencePercent}%",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = c,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🟢 Zero anomalous frame segments detected across video timeline.",
                    style = MaterialTheme.typography.bodySmall,
                    color = SafeEmerald
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Disclaimer: AI deepfake detection can produce false positives or false negatives depending on video compression.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
