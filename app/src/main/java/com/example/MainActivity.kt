package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AlertSeverity
import com.example.model.ThreatStatus
import com.example.ui.components.CyberGridBackground
import com.example.ui.screens.AlertsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.ScanHubScreen
import com.example.ui.screens.ScanReportDetailOverlay
import com.example.ui.screens.ShieldAiScreen
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.SafeEmerald
import com.example.ui.theme.ScamShieldTheme
import com.example.ui.theme.ThreatCrimson
import com.example.viewmodel.MainNavTab
import com.example.viewmodel.ScamShieldViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ScamShieldViewModel = viewModel()
            val settings by viewModel.settings.collectAsStateWithLifecycle()

            ScamShieldTheme(darkTheme = settings.darkMode) {
                ScamShieldApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ScamShieldApp(viewModel: ScamShieldViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val activeScanner by viewModel.activeScanner.collectAsStateWithLifecycle()
    val selectedReport by viewModel.selectedReportDetail.collectAsStateWithLifecycle()
    val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()
    val scanProgress by viewModel.scanProgress.collectAsStateWithLifecycle()
    val scanStepLabel by viewModel.scanStepLabel.collectAsStateWithLifecycle()
    val reports by viewModel.reports.collectAsStateWithLifecycle()
    val alerts by viewModel.alerts.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isAiTyping by viewModel.isAiTyping.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val installedApps by viewModel.installedApps.collectAsStateWithLifecycle()
    val radarMetrics by viewModel.radarMetrics.collectAsStateWithLifecycle()

    val securityScore = viewModel.computeUnifiedSecurityScore()
    val criticalAlertCount = alerts.count { it.severity == AlertSeverity.CRITICAL || it.severity == AlertSeverity.HIGH }

    // Handle Back navigation cleanly from secondary tabs back to Home
    if (currentTab != MainNavTab.HOME && activeScanner == null && selectedReport == null) {
        BackHandler {
            viewModel.selectTab(MainNavTab.HOME)
        }
    }

    CyberGridBackground(isDark = settings.darkMode) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets.safeDrawing,
                topBar = {
                    ScamShieldTopBar(
                        voiceEnabled = settings.voiceFeedbackEnabled,
                        onToggleVoice = {
                            viewModel.updateSettings { it.copy(voiceFeedbackEnabled = !it.voiceFeedbackEnabled) }
                        },
                        onQuickScanTap = {
                            viewModel.selectTab(MainNavTab.SCAN)
                        }
                    )
                },
                bottomBar = {
                    ScamShieldBottomNavigation(
                        currentTab = currentTab,
                        criticalAlertCount = criticalAlertCount,
                        onSelectTab = { tab ->
                            if (tab != MainNavTab.SCAN) {
                                viewModel.closeActiveScanner()
                            }
                            viewModel.selectTab(tab)
                        }
                    )
                }
            ) { innerPadding ->
                Crossfade(
                    targetState = currentTab,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    label = "main_tab_crossfade"
                ) { tab ->
                    when (tab) {
                        MainNavTab.HOME -> DashboardScreen(
                            securityScore = securityScore,
                            reports = reports,
                            alerts = alerts,
                            radarMetrics = radarMetrics,
                            onOpenScanner = { scanner -> viewModel.openScanner(scanner) },
                            onOpenReport = { report -> viewModel.openReportDetail(report) },
                            onNavigateToScanHub = { viewModel.selectTab(MainNavTab.SCAN) },
                            onNavigateToAlerts = { viewModel.selectTab(MainNavTab.ALERTS) },
                            onNavigateToShieldAi = { viewModel.selectTab(MainNavTab.SHIELD_AI) }
                        )

                        MainNavTab.SCAN -> ScanHubScreen(
                            activeScanner = activeScanner,
                            isScanning = isScanning,
                            scanProgress = scanProgress,
                            scanStepLabel = scanStepLabel,
                            installedApps = installedApps,
                            onSelectScanner = { scanner -> viewModel.openScanner(scanner) },
                            onCloseScanner = { viewModel.closeActiveScanner() },
                            onRunScan = { scanner, payload, platform ->
                                viewModel.runSecurityScan(scanner, payload, platform)
                            }
                        )

                        MainNavTab.ALERTS -> AlertsScreen(
                            alerts = alerts
                        )

                        MainNavTab.REPORTS -> ReportsScreen(
                            reports = reports,
                            onOpenReport = { report -> viewModel.openReportDetail(report) }
                        )

                        MainNavTab.SHIELD_AI -> ShieldAiScreen(
                            messages = chatMessages,
                            isAiTyping = isAiTyping,
                            latestReport = reports.firstOrNull(),
                            onSendMessage = { msg -> viewModel.sendShieldAiMessage(msg) }
                        )

                        MainNavTab.PROFILE -> ProfileSettingsScreen(
                            settings = settings,
                            totalScans = reports.size,
                            threatsBlocked = reports.count { it.status == ThreatStatus.HIGH_RISK },
                            onUpdateSettings = { transform -> viewModel.updateSettings(transform) },
                            onTestSensoryAlert = { status ->
                                viewModel.feedbackManager.triggerScanResultFeedback(status, settings)
                            },
                            onResetDemoHistory = { viewModel.clearAndResetDemoHistory() }
                        )
                    }
                }
            }

            // Full-screen Forensic Report Modal Overlay when a scan completes or a report is selected
            val activeReport = selectedReport
            if (activeReport != null) {
                ScanReportDetailOverlay(
                    report = activeReport,
                    onDismiss = { viewModel.closeReportDetail() },
                    onReplayVoice = { rep -> viewModel.replayReportVoiceFeedback(rep) },
                    onAskShieldAi = { rep -> viewModel.askShieldAiAboutReport(rep) }
                )
            }
        }
    }
}

@Composable
private fun ScamShieldTopBar(
    voiceEnabled: Boolean,
    onToggleVoice: () -> Unit,
    onQuickScanTap: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onQuickScanTap() }
            ) {
                Surface(
                    shape = CircleShape,
                    color = ElectricBlue.copy(alpha = 0.22f),
                    border = BorderStroke(1.dp, CyberCyan)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Security,
                        contentDescription = "ScamShield AI Logo",
                        tint = CyberCyan,
                        modifier = Modifier
                            .padding(7.dp)
                            .size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "ScamShield AI",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Digital Risk Protection",
                        style = MaterialTheme.typography.labelSmall,
                        color = CyberCyan
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (voiceEnabled) SafeEmerald.copy(alpha = 0.16f) else MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, if (voiceEnabled) SafeEmerald.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline),
                    modifier = Modifier
                        .clickable { onToggleVoice() }
                        .testTag("top_voice_toggle")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.RecordVoiceOver,
                            contentDescription = "Voice Safety Feedback",
                            tint = if (voiceEnabled) SafeEmerald else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (voiceEnabled) "Voice ON" else "Voice OFF",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (voiceEnabled) SafeEmerald else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScamShieldBottomNavigation(
    currentTab: MainNavTab,
    criticalAlertCount: Int,
    onSelectTab: (MainNavTab) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 8.dp,
        windowInsets = WindowInsets.navigationBars
    ) {
        MainNavTab.entries.forEach { tab ->
            val selected = currentTab == tab
            val icon = when (tab) {
                MainNavTab.HOME -> if (selected) Icons.Filled.Home else Icons.Outlined.Home
                MainNavTab.SCAN -> if (selected) Icons.Filled.Radar else Icons.Outlined.Radar
                MainNavTab.ALERTS -> if (selected) Icons.Filled.NotificationsActive else Icons.Outlined.Notifications
                MainNavTab.REPORTS -> if (selected) Icons.Filled.Assessment else Icons.Outlined.Assessment
                MainNavTab.SHIELD_AI -> if (selected) Icons.Filled.SmartToy else Icons.Outlined.SmartToy
                MainNavTab.PROFILE -> if (selected) Icons.Filled.Person else Icons.Outlined.Person
            }

            NavigationBarItem(
                selected = selected,
                onClick = { onSelectTab(tab) },
                modifier = Modifier.testTag("nav_tab_${tab.route}"),
                icon = {
                    if (tab == MainNavTab.ALERTS && criticalAlertCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = ThreatCrimson) {
                                    Text("$criticalAlertCount")
                                }
                            }
                        ) {
                            Icon(imageVector = icon, contentDescription = tab.label)
                        }
                    } else {
                        Icon(imageVector = icon, contentDescription = tab.label)
                    }
                },
                label = {
                    Text(
                        text = tab.label,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CyberCyan,
                    selectedTextColor = CyberCyan,
                    indicatorColor = ElectricBlue.copy(alpha = 0.25f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
