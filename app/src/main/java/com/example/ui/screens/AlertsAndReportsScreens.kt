package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AlertSeverity
import com.example.model.ScanReport
import com.example.model.SecurityAlert
import com.example.model.ThreatStatus
import com.example.ui.components.GlassmorphicCyberCard
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.HighOrange
import com.example.ui.theme.SafeEmerald
import com.example.ui.theme.SuspiciousAmber
import com.example.ui.theme.ThreatCrimson

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlertsScreen(
    alerts: List<SecurityAlert>,
    modifier: Modifier = Modifier
) {
    var selectedSeverity by remember { mutableStateOf<AlertSeverity?>(null) }

    val filteredAlerts = remember(alerts, selectedSeverity) {
        if (selectedSeverity == null) alerts
        else alerts.filter { it.severity == selectedSeverity }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("alerts_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.NotificationsActive,
                        contentDescription = null,
                        tint = ThreatCrimson,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Real-Time Alert Center",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Live security telemetry notifications across phishing, impersonation, deepfakes, and APK audits.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Severity Filter Chips: All, 🔴 Critical, 🟠 High, 🟡 Medium, 🟢 Safe
        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedSeverity == null,
                    onClick = { selectedSeverity = null },
                    label = { Text("All (${alerts.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricBlue.copy(alpha = 0.28f),
                        selectedLabelColor = CyberCyan
                    )
                )
                AlertSeverity.entries.forEach { severity ->
                    val count = alerts.count { it.severity == severity }
                    val c = severityColor(severity)
                    FilterChip(
                        selected = selectedSeverity == severity,
                        onClick = { selectedSeverity = severity },
                        label = { Text("${severity.emoji} ${severity.label} ($count)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = c.copy(alpha = 0.24f),
                            selectedLabelColor = c
                        )
                    )
                }
            }
        }

        items(filteredAlerts, key = { it.id }) { alert ->
            AlertNotificationCard(alert = alert)
        }
    }
}

private fun severityColor(severity: AlertSeverity): Color = when (severity) {
    AlertSeverity.CRITICAL -> ThreatCrimson
    AlertSeverity.HIGH -> HighOrange
    AlertSeverity.MEDIUM -> SuspiciousAmber
    AlertSeverity.SAFE -> SafeEmerald
}

@Composable
private fun AlertNotificationCard(alert: SecurityAlert) {
    val accent = severityColor(alert.severity)
    GlassmorphicCyberCard(
        accentColor = accent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = accent.copy(alpha = 0.18f),
                    border = BorderStroke(1.dp, accent.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "${alert.severity.emoji} ${alert.severity.label.uppercase()}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = accent,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = alert.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = alert.scannerType.emoji,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = alert.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = alert.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Source: ${alert.sourceTarget}",
                    style = MaterialTheme.typography.labelSmall,
                    color = CyberCyan
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = alert.actionTaken,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = accent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

enum class ReportFilterTab(val label: String) {
    ALL("All"),
    SAFE("Safe"),
    SUSPICIOUS("Suspicious"),
    THREATS("Threats")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReportsScreen(
    reports: List<ScanReport>,
    onOpenReport: (ScanReport) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeFilter by remember { mutableStateOf(ReportFilterTab.ALL) }

    val filteredReports = remember(reports, activeFilter) {
        when (activeFilter) {
            ReportFilterTab.ALL -> reports
            ReportFilterTab.SAFE -> reports.filter { it.status == ThreatStatus.SAFE }
            ReportFilterTab.SUSPICIOUS -> reports.filter { it.status == ThreatStatus.SUSPICIOUS }
            ReportFilterTab.THREATS -> reports.filter { it.status == ThreatStatus.HIGH_RISK }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("reports_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Assessment,
                        contentDescription = null,
                        tint = CyberCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Forensic Security Reports",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Every scan generates a verifiable report containing Threat Category, Evidence, Explainable AI, and Smart Recommendations.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Filter Chips: All, Safe, Suspicious, Threats
        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ReportFilterTab.entries.forEach { tab ->
                    val count = when (tab) {
                        ReportFilterTab.ALL -> reports.size
                        ReportFilterTab.SAFE -> reports.count { it.status == ThreatStatus.SAFE }
                        ReportFilterTab.SUSPICIOUS -> reports.count { it.status == ThreatStatus.SUSPICIOUS }
                        ReportFilterTab.THREATS -> reports.count { it.status == ThreatStatus.HIGH_RISK }
                    }
                    FilterChip(
                        selected = activeFilter == tab,
                        onClick = { activeFilter = tab },
                        label = { Text("${tab.label} ($count)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricBlue.copy(alpha = 0.28f),
                            selectedLabelColor = CyberCyan
                        ),
                        modifier = Modifier.testTag("report_filter_${tab.name.lowercase()}")
                    )
                }
            }
        }

        if (filteredReports.isEmpty()) {
            item {
                GlassmorphicCyberCard(accentColor = CyberCyan) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Shield,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No Reports in This Category",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Run a security scan from the Scan tab to generate new forensic reports.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredReports, key = { it.id }) { report ->
                TimelineScanRowCard(
                    report = report,
                    onClick = { onOpenReport(report) }
                )
            }
        }
    }
}
