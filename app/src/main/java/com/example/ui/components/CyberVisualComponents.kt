package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ThreatRadarMetrics
import com.example.model.ThreatStatus
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberCyanGlow
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SafeEmerald
import com.example.ui.theme.SuspiciousAmber
import com.example.ui.theme.ThreatCrimson
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun statusColor(status: ThreatStatus): Color = when (status) {
    ThreatStatus.SAFE -> SafeEmerald
    ThreatStatus.SUSPICIOUS -> SuspiciousAmber
    ThreatStatus.HIGH_RISK -> ThreatCrimson
}

@Composable
fun CyberGridBackground(
    modifier: Modifier = Modifier,
    isDark: Boolean = true,
    content: @Composable () -> Unit
) {
    val bgColor = MaterialTheme.colorScheme.background
    val gridColor = if (isDark) CyberCyan.copy(alpha = 0.05f) else ElectricBlue.copy(alpha = 0.05f)
    val glowTopColor = if (isDark) ElectricBlue.copy(alpha = 0.16f) else ElectricBlue.copy(alpha = 0.08f)
    val glowBottomColor = if (isDark) NeonPurple.copy(alpha = 0.12f) else NeonPurple.copy(alpha = 0.06f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Top ambient radial glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(glowTopColor, Color.Transparent),
                    center = Offset(size.width * 0.85f, size.height * 0.1f),
                    radius = size.width * 0.75f
                ),
                center = Offset(size.width * 0.85f, size.height * 0.1f),
                radius = size.width * 0.75f
            )
            // Bottom ambient radial glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(glowBottomColor, Color.Transparent),
                    center = Offset(size.width * 0.15f, size.height * 0.8f),
                    radius = size.width * 0.7f
                ),
                center = Offset(size.width * 0.15f, size.height * 0.8f),
                radius = size.width * 0.7f
            )

            // Subtle cyber grid lines
            val step = 36.dp.toPx()
            var x = 0f
            while (x < size.width) {
                drawLine(
                    color = gridColor,
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 1f
                )
                x += step
            }
            var y = 0f
            while (y < size.height) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
                y += step
            }
        }
        content()
    }
}

@Composable
fun GlassmorphicCyberCard(
    modifier: Modifier = Modifier,
    accentColor: Color = CyberCyan,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.86f)
    val borderBrush = Brush.linearGradient(
        colors = listOf(
            accentColor.copy(alpha = 0.45f),
            MaterialTheme.colorScheme.outline.copy(alpha = 0.18f),
            accentColor.copy(alpha = 0.22f)
        )
    )
    val cardModifier = if (onClick != null) {
        modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    } else {
        modifier.clip(RoundedCornerShape(20.dp))
    }

    Card(
        modifier = cardModifier.border(
            width = 1.dp,
            brush = borderBrush,
            shape = RoundedCornerShape(20.dp)
        ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        content()
    }
}

@Composable
fun StatusPillBadge(
    status: ThreatStatus,
    modifier: Modifier = Modifier,
    customLabel: String? = null
) {
    val color = statusColor(status)
    val icon = when (status) {
        ThreatStatus.SAFE -> Icons.Filled.CheckCircle
        ThreatStatus.SUSPICIOUS -> Icons.Filled.Warning
        ThreatStatus.HIGH_RISK -> Icons.Filled.GppBad
    }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.16f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.55f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = status.label,
                tint = color,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = customLabel ?: "${status.badgeEmoji} ${status.label}",
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AnimatedCircularRiskGauge(
    score: Int,
    label: String,
    status: ThreatStatus,
    modifier: Modifier = Modifier,
    size: Dp = 156.dp,
    isSecurityPostureMode: Boolean = false
) {
    val targetSweep = (score.coerceIn(0, 100) / 100f) * 260f
    val animatedSweep by animateFloatAsState(
        targetValue = targetSweep,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "gauge_sweep"
    )

    val primaryColor by animateColorAsState(
        targetValue = if (isSecurityPostureMode) {
            when {
                score >= 80 -> SafeEmerald
                score >= 55 -> SuspiciousAmber
                else -> ThreatCrimson
            }
        } else {
            statusColor(status)
        },
        animationSpec = tween(500),
        label = "gauge_color"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 13.dp.toPx()
            val arcDiameter = this.size.minDimension - strokeWidth * 2f
            val topLeft = Offset(strokeWidth, strokeWidth)

            // Outer subtle pulsing glow
            drawCircle(
                color = primaryColor.copy(alpha = 0.11f),
                radius = (this.size.minDimension / 2f) * pulseScale
            )

            // Background track arc
            drawArc(
                color = primaryColor.copy(alpha = 0.16f),
                startAngle = 140f,
                sweepAngle = 260f,
                useCenter = false,
                topLeft = topLeft,
                size = Size(arcDiameter, arcDiameter),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Foreground animated neon arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.75f),
                        primaryColor,
                        CyberCyanGlow,
                        primaryColor
                    )
                ),
                startAngle = 140f,
                sweepAngle = animatedSweep,
                useCenter = false,
                topLeft = topLeft,
                size = Size(arcDiameter, arcDiameter),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$score",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "/ 100",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(50),
                color = primaryColor.copy(alpha = 0.18f),
                border = BorderStroke(1.dp, primaryColor.copy(alpha = 0.5f))
            ) {
                Text(
                    text = label,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ThreatRadarChart(
    metrics: ThreatRadarMetrics,
    modifier: Modifier = Modifier
) {
    val axes = listOf(
        "Phishing" to metrics.phishing,
        "Social Scam" to metrics.socialScam,
        "Financial" to metrics.financialScam,
        "Fake ID" to metrics.fakeIdentity,
        "Malware" to metrics.malwareRisk,
        "Deepfake" to metrics.mediaManipulation
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(180.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val maxRadius = size.minDimension / 2.2f
                val numAxes = axes.size

                // Draw 4 concentric hexagon web levels
                for (level in 1..4) {
                    val r = maxRadius * (level / 4f)
                    val webPath = Path()
                    for (i in 0 until numAxes) {
                        val angle = (2 * PI * i / numAxes) - (PI / 2)
                        val px = center.x + (r * cos(angle)).toFloat()
                        val py = center.y + (r * sin(angle)).toFloat()
                        if (i == 0) webPath.moveTo(px, py) else webPath.lineTo(px, py)
                    }
                    webPath.close()
                    drawPath(
                        path = webPath,
                        color = CyberCyan.copy(alpha = 0.18f),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                // Draw spokes
                for (i in 0 until numAxes) {
                    val angle = (2 * PI * i / numAxes) - (PI / 2)
                    val endX = center.x + (maxRadius * cos(angle)).toFloat()
                    val endY = center.y + (maxRadius * sin(angle)).toFloat()
                    drawLine(
                        color = CyberCyan.copy(alpha = 0.22f),
                        start = center,
                        end = Offset(endX, endY),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // Draw radar polygon for threat metrics
                val dataPath = Path()
                val vertices = mutableListOf<Offset>()
                for (i in 0 until numAxes) {
                    val value = axes[i].second.coerceIn(0.1f, 1.0f)
                    val angle = (2 * PI * i / numAxes) - (PI / 2)
                    val px = center.x + (maxRadius * value * cos(angle)).toFloat()
                    val py = center.y + (maxRadius * value * sin(angle)).toFloat()
                    vertices.add(Offset(px, py))
                    if (i == 0) dataPath.moveTo(px, py) else dataPath.lineTo(px, py)
                }
                dataPath.close()

                drawPath(
                    path = dataPath,
                    brush = Brush.radialGradient(
                        colors = listOf(
                            CyberCyan.copy(alpha = 0.42f),
                            NeonPurple.copy(alpha = 0.28f),
                            ThreatCrimson.copy(alpha = 0.22f)
                        ),
                        center = center,
                        radius = maxRadius
                    )
                )
                drawPath(
                    path = dataPath,
                    color = CyberCyanGlow,
                    style = Stroke(width = 2.2.dp.toPx())
                )

                // Draw glowing node dots
                vertices.forEachIndexed { idx, pt ->
                    val valScore = axes[idx].second
                    val dotColor = when {
                        valScore >= 0.7f -> ThreatCrimson
                        valScore >= 0.5f -> SuspiciousAmber
                        else -> SafeEmerald
                    }
                    drawCircle(color = dotColor.copy(alpha = 0.35f), radius = 7.dp.toPx(), center = pt)
                    drawCircle(color = dotColor, radius = 4.dp.toPx(), center = pt)
                }
            }
        }

        // Radar Axis Legend Chips
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            axes.chunked(3).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { (name, fraction) ->
                        val pct = (fraction * 100).toInt()
                        val chipColor = when {
                            pct >= 70 -> ThreatCrimson
                            pct >= 50 -> SuspiciousAmber
                            else -> SafeEmerald
                        }
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            border = BorderStroke(1.dp, chipColor.copy(alpha = 0.35f))
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                                Text(
                                    text = "$pct%",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = chipColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ThreatDistributionBar(
    safeCount: Int,
    suspiciousCount: Int,
    threatCount: Int,
    modifier: Modifier = Modifier
) {
    val total = (safeCount + suspiciousCount + threatCount).coerceAtLeast(1)
    val safeWeight = (safeCount.toFloat() / total).coerceAtLeast(0.08f)
    val suspWeight = (suspiciousCount.toFloat() / total).coerceAtLeast(0.08f)
    val threatWeight = (threatCount.toFloat() / total).coerceAtLeast(0.08f)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(50))
        ) {
            Box(
                modifier = Modifier
                    .weight(safeWeight)
                    .fillMaxSize()
                    .background(SafeEmerald)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Box(
                modifier = Modifier
                    .weight(suspWeight)
                    .fillMaxSize()
                    .background(SuspiciousAmber)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Box(
                modifier = Modifier
                    .weight(threatWeight)
                    .fillMaxSize()
                    .background(ThreatCrimson)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LegendDotItem("Safe ($safeCount)", SafeEmerald)
            LegendDotItem("Suspicious ($suspiciousCount)", SuspiciousAmber)
            LegendDotItem("Threats ($threatCount)", ThreatCrimson)
        }
    }
}

@Composable
private fun LegendDotItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PrivacyFirstNoticeCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = CyberCyan.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Privacy First",
                tint = CyberCyan,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Privacy-First Guarantee: Never enter real OTP codes, passwords, or PINs. ScamShield AI analyzes message patterns and threat indicators locally & securely.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
