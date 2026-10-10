package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.AlertSeverity
import com.example.model.ScanReport
import com.example.model.ScannerType
import com.example.model.SecurityAlert
import com.example.model.ThreatRadarMetrics
import com.example.model.ThreatStatus
import com.example.ui.components.AnimatedCircularRiskGauge
import com.example.ui.components.GlassmorphicCyberCard
import com.example.ui.components.PrivacyFirstNoticeCard
import com.example.ui.components.StatusPillBadge
import com.example.ui.components.ThreatDistributionBar
import com.example.ui.components.ThreatRadarChart
import com.example.ui.components.statusColor
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberObsidian
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SafeEmerald
import com.example.ui.theme.SuspiciousAmber
import com.example.ui.theme.ThreatCrimson

@Composable
fun DashboardScreen(
    securityScore: Int,
    reports: List<ScanReport>,
    alerts: List<SecurityAlert>,
    radarMetrics: ThreatRadarMetrics,
    onOpenScanner: (ScannerType) -> Unit,
    onOpenReport: (ScanReport) -> Unit,
    onNavigateToScanHub: () -> Unit,
    onNavigateToAlerts: () -> Unit,
    onNavigateToShieldAi: () -> Unit,
    modifier: Modifier = Modifier
) {
    val threatsDetectedCount = reports.count { it.status == ThreatStatus.HIGH_RISK }
    val suspiciousCount = reports.count { it.status == ThreatStatus.SUSPICIOUS }
    val safeScansCount = reports.count { it.status == ThreatStatus.SAFE }
    val criticalAlertsCount = alerts.count { it.severity == AlertSeverity.CRITICAL || it.severity == AlertSeverity.HIGH }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Hero Security Score & Command Banner
        item {
            GlassmorphicCyberCard(
                accentColor = CyberCyan,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_cyber_hero_banner_1791532015872),
                        contentDescription = "ScamShield AI Threat Defense Banner",
                        contentScale = ContentScale.Crop,
                        alpha = 0.24f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(235.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        CyberObsidian.copy(alpha = 0.45f),
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.94f)
                                    )
                                )
                            )
                            .padding(18.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = CyberCyan.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, CyberCyan)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Security,
                                            contentDescription = "Shield Active",
                                            tint = CyberCyan,
                                            modifier = Modifier
                                                .padding(6.dp)
                                                .size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "UNIFIED SECURITY POSTURE",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = CyberCyan
                                        )
                                        Text(
                                            text = "AI Real-Time Protection Active",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = SafeEmerald.copy(alpha = 0.18f),
                                    border = BorderStroke(1.dp, SafeEmerald.copy(alpha = 0.6f))
                                ) {
                                    Text(
                                        text = "🟢 LOW RISK",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SafeEmerald,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                AnimatedCircularRiskGauge(
                                    score = securityScore,
                                    label = "PROTECTED",
                                    status = ThreatStatus.SAFE,
                                    size = 138.dp,
                                    isSecurityPostureMode = true
                                )

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Security Score: $securityScore/100",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Combines telemetry across URL, OTP, Financial, Deepfake & APK scanners.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = onNavigateToScanHub,
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                                            modifier = Modifier.testTag("hero_quick_scan_btn")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Bolt,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Scan Now", style = MaterialTheme.typography.labelLarge)
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = NeonPurple.copy(alpha = 0.2f),
                                            border = BorderStroke(1.dp, NeonPurple.copy(alpha = 0.6f)),
                                            modifier = Modifier
                                                .clickable { onNavigateToShieldAi() }
                                                .testTag("hero_ask_ai_btn")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.SmartToy,
                                                    contentDescription = "Ask Shield AI",
                                                    tint = NeonPurple,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "Shield AI",
                                                    style = MaterialTheme.typography.labelLarge,
                                                    color = NeonPurple
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. 2x2 Bento Security Metrics Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BentoStatCard(
                        title = "Threats Detected",
                        value = "$threatsDetectedCount",
                        subtitle = "🔴 High-Risk Blocked",
                        accentColor = ThreatCrimson,
                        icon = Icons.Filled.GppBad,
                        onClick = onNavigateToAlerts,
                        modifier = Modifier.weight(1f)
                    )
                    BentoStatCard(
                        title = "Safe Scans",
                        value = "$safeScansCount",
                        subtitle = "🟢 Verified Clean",
                        accentColor = SafeEmerald,
                        icon = Icons.Filled.CheckCircle,
                        onClick = onNavigateToScanHub,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BentoStatCard(
                        title = "Critical Alerts",
                        value = "$criticalAlertsCount",
                        subtitle = "🚨 Active Warnings",
                        accentColor = SuspiciousAmber,
                        icon = Icons.Filled.NotificationsActive,
                        onClick = onNavigateToAlerts,
                        modifier = Modifier.weight(1f)
                    )
                    BentoStatCard(
                        title = "Security Activity",
                        value = "${reports.size} Scans",
                        subtitle = "⚡ 8 AI Engines Ready",
                        accentColor = CyberCyan,
                        icon = Icons.Filled.Timeline,
                        onClick = onNavigateToScanHub,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 3. Quick Launch 8 Security Scanners Carousel
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AI Threat Scanners",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "View All 8 →",
                        style = MaterialTheme.typography.labelLarge,
                        color = CyberCyan,
                        modifier = Modifier.clickable { onNavigateToScanHub() }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(ScannerType.entries) { scanner ->
                        QuickScannerPillCard(
                            scanner = scanner,
                            onClick = { onOpenScanner(scanner) }
                        )
                    }
                }
            }
        }

        // 4. Threat Radar & Threat Distribution
        item {
            GlassmorphicCyberCard(accentColor = NeonPurple) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Radar,
                                contentDescription = "Threat Radar",
                                tint = CyberCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Multi-Vector Threat Radar",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Real-time risk surface across 6 attack vectors",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    ThreatRadarChart(metrics = radarMetrics)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Threat Distribution",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ThreatDistributionBar(
                        safeCount = safeScansCount,
                        suspiciousCount = suspiciousCount,
                        threatCount = threatsDetectedCount
                    )
                }
            }
        }

        // 5. Security Timeline (Recent Scans)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Security Timeline & Recent Scans",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Tap for AI Report",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(reports.take(5), key = { it.id }) { report ->
            TimelineScanRowCard(
                report = report,
                onClick = { onOpenReport(report) }
            )
        }

        item {
            PrivacyFirstNoticeCard()
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun BentoStatCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassmorphicCyberCard(
        modifier = modifier,
        accentColor = accentColor,
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Surface(
                    shape = CircleShape,
                    color = accentColor.copy(alpha = 0.16f)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier
                            .padding(6.dp)
                            .size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = accentColor
            )
        }
    }
}

@Composable
private fun QuickScannerPillCard(
    scanner: ScannerType,
    onClick: () -> Unit
) {
    GlassmorphicCyberCard(
        accentColor = CyberCyan,
        onClick = onClick,
        modifier = Modifier
            .width(155.dp)
            .testTag("quick_scanner_${scanner.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = scanner.emoji,
                    style = MaterialTheme.typography.headlineMedium
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ElectricBlue.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = scanner.badgeTag,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = CyberCyan
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = scanner.shortTitle,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = scanner.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TimelineScanRowCard(
    report: ScanReport,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = statusColor(report.status)
    GlassmorphicCyberCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("report_card_${report.id}"),
        accentColor = accent,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left timeline status indicator
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(46.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = accent.copy(alpha = 0.18f),
                    border = BorderStroke(1.dp, accent.copy(alpha = 0.6f)),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = report.scannerType.emoji,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${report.riskScore}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = accent,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = report.scannerType.title,
                        style = MaterialTheme.typography.labelMedium,
                        color = CyberCyan
                    )
                    Text(
                        text = report.timestamp,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = report.targetSummary,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = report.headlineSummary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                StatusPillBadge(status = report.status)
            }

            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open Report",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
