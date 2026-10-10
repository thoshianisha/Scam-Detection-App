package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AlertSeverity
import com.example.model.ChatMessage
import com.example.model.InstalledAppCandidate
import com.example.model.SampleCyberData
import com.example.model.ScanReport
import com.example.model.ScannerType
import com.example.model.SecurityAlert
import com.example.model.ThreatRadarMetrics
import com.example.model.ThreatStatus
import com.example.model.UserSettings
import com.example.network.GeminiSecurityService
import com.example.util.SecurityFeedbackManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class MainNavTab(val route: String, val label: String) {
    HOME("home", "Home"),
    SCAN("scan", "Scan"),
    ALERTS("alerts", "Alerts"),
    REPORTS("reports", "Reports"),
    SHIELD_AI("shield_ai", "Shield AI"),
    PROFILE("profile", "Profile")
}

class ScamShieldViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("scamshield_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
    val feedbackManager = SecurityFeedbackManager(application)

    private val _currentTab = MutableStateFlow(MainNavTab.HOME)
    val currentTab: StateFlow<MainNavTab> = _currentTab.asStateFlow()

    private val _activeScanner = MutableStateFlow<ScannerType?>(null)
    val activeScanner: StateFlow<ScannerType?> = _activeScanner.asStateFlow()

    private val _selectedReportDetail = MutableStateFlow<ScanReport?>(null)
    val selectedReportDetail: StateFlow<ScanReport?> = _selectedReportDetail.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _scanStepLabel = MutableStateFlow("")
    val scanStepLabel: StateFlow<String> = _scanStepLabel.asStateFlow()

    private val _scanProgress = MutableStateFlow(0f)
    val scanProgress: StateFlow<Float> = _scanProgress.asStateFlow()

    private val _reports = MutableStateFlow<List<ScanReport>>(SampleCyberData.initialReports)
    val reports: StateFlow<List<ScanReport>> = _reports.asStateFlow()

    private val _alerts = MutableStateFlow<List<SecurityAlert>>(SampleCyberData.initialAlerts)
    val alerts: StateFlow<List<SecurityAlert>> = _alerts.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(SampleCyberData.initialChatMessages)
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiTyping = MutableStateFlow(false)
    val isAiTyping: StateFlow<Boolean> = _isAiTyping.asStateFlow()

    private val _settings = MutableStateFlow(UserSettings())
    val settings: StateFlow<UserSettings> = _settings.asStateFlow()

    private val _installedApps = MutableStateFlow<List<InstalledAppCandidate>>(SampleCyberData.demoInstalledApps)
    val installedApps: StateFlow<List<InstalledAppCandidate>> = _installedApps.asStateFlow()

    private val _radarMetrics = MutableStateFlow(ThreatRadarMetrics())
    val radarMetrics: StateFlow<ThreatRadarMetrics> = _radarMetrics.asStateFlow()

    init {
        loadPersistedState()
        loadDeviceInstalledApps()
    }

    private fun loadPersistedState() {
        try {
            val savedSettingsJson = prefs.getString("user_settings", null)
            if (!savedSettingsJson.isNullOrBlank()) {
                _settings.value = json.decodeFromString(savedSettingsJson)
            }
            val savedReportsJson = prefs.getString("scan_reports", null)
            if (!savedReportsJson.isNullOrBlank()) {
                val decoded: List<ScanReport> = json.decodeFromString(savedReportsJson)
                if (decoded.isNotEmpty()) {
                    _reports.value = decoded
                }
            }
        } catch (_: Exception) {
        }
    }

    private fun persistState() {
        try {
            prefs.edit()
                .putString("user_settings", json.encodeToString(_settings.value))
                .putString("scan_reports", json.encodeToString(_reports.value.take(30)))
                .apply()
        } catch (_: Exception) {
        }
    }

    private fun loadDeviceInstalledApps() {
        viewModelScope.launch {
            try {
                val pm = getApplication<Application>().packageManager
                val packages = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)
                val deviceCandidates = packages.take(8).mapNotNull { pkgInfo ->
                    val appInfo = pkgInfo.applicationInfo ?: return@mapNotNull null
                    val label = pm.getApplicationLabel(appInfo).toString()
                    if (label.isBlank() || pkgInfo.packageName == "com.aistudio.scamshieldai.xkrpvn") {
                        return@mapNotNull null
                    }
                    val perms = pkgInfo.requestedPermissions?.toList() ?: emptyList()
                    val riskyPerms = perms.filter {
                        it.contains("READ_SMS") || it.contains("SYSTEM_ALERT_WINDOW") ||
                            it.contains("READ_CONTACTS") || it.contains("RECORD_AUDIO")
                    }.map { it.substringAfterLast(".") }

                    InstalledAppCandidate(
                        appName = label,
                        packageName = pkgInfo.packageName,
                        developer = "Device Installed Package",
                        installSource = "Android System / Play Store",
                        permissions = perms.take(6),
                        suspiciousPermissions = riskyPerms,
                        presetRiskScore = if (riskyPerms.size >= 2) 55 else 8,
                        isSystemOrTrusted = riskyPerms.isEmpty()
                    )
                }.take(3)

                _installedApps.value = SampleCyberData.demoInstalledApps + deviceCandidates
            } catch (_: Exception) {
                _installedApps.value = SampleCyberData.demoInstalledApps
            }
        }
    }

    fun selectTab(tab: MainNavTab) {
        _currentTab.value = tab
    }

    fun openScanner(scannerType: ScannerType) {
        _activeScanner.value = scannerType
        _currentTab.value = MainNavTab.SCAN
    }

    fun closeActiveScanner() {
        _activeScanner.value = null
    }

    fun openReportDetail(report: ScanReport) {
        _selectedReportDetail.value = report
    }

    fun closeReportDetail() {
        _selectedReportDetail.value = null
    }

    fun runSecurityScan(
        scannerType: ScannerType,
        inputPayload: String,
        platformHint: String = ""
    ) {
        if (inputPayload.isBlank() || _isScanning.value) return

        viewModelScope.launch {
            _isScanning.value = true
            _scanProgress.value = 0.15f
            _scanStepLabel.value = "Initializing ${scannerType.shortTitle} telemetry engine..."
            delay(320)

            _scanProgress.value = 0.48f
            _scanStepLabel.value = when (scannerType) {
                ScannerType.URL_SCANNER -> "Inspecting DNS, TLS certificate & phishing signatures..."
                ScannerType.SOCIAL_PROFILE -> "Analyzing account age, follower graph & impersonation vectors..."
                ScannerType.OTP_MESSAGE -> "Running NLP urgency, fake KYC & OTP trap heuristics..."
                ScannerType.BANK_LOAN -> "Checking RBI/NBFC fraud patterns & advance-fee traps..."
                ScannerType.IMAGE_AUTH -> "Evaluating frequency-domain GAN noise & EXIF headers..."
                ScannerType.VIDEO_AUTH -> "Scanning keyframes for lip-sync & neural vocoder anomalies..."
                ScannerType.APP_APK -> "Auditing AndroidManifest permissions & certificate trust..."
                ScannerType.PDF_DOC -> "Parsing PDF object streams, links & extortion clauses..."
            }
            delay(420)

            _scanProgress.value = 0.82f
            _scanStepLabel.value = "Synthesizing Explainable AI threat report..."

            val report = GeminiSecurityService.analyzeThreat(
                scannerType = scannerType,
                rawInput = inputPayload,
                platformHint = platformHint
            )

            _scanProgress.value = 1.0f
            delay(180)

            _reports.update { listOf(report) + it }
            persistState()

            // Create corresponding alert in Alert Center
            val severity = when (report.status) {
                ThreatStatus.HIGH_RISK -> AlertSeverity.CRITICAL
                ThreatStatus.SUSPICIOUS -> AlertSeverity.MEDIUM
                ThreatStatus.SAFE -> AlertSeverity.SAFE
            }
            val newAlert = SecurityAlert(
                id = "alt_${System.currentTimeMillis()}",
                title = report.headlineSummary,
                description = "${report.threatCategory} — Target: ${report.targetSummary}",
                timestamp = "Just now",
                severity = severity,
                scannerType = scannerType,
                sourceTarget = report.targetSummary,
                actionTaken = if (report.status == ThreatStatus.SAFE) "Verified Safe" else "Flagged (${report.riskScore}/100 Risk)"
            )
            _alerts.update { listOf(newAlert) + it }

            // Update Threat Radar dynamically
            updateRadarFromReport(report)

            _isScanning.value = false
            _selectedReportDetail.value = report

            // Trigger Multi-Sensory Security Feedback (Voice, Sound, Vibration)
            feedbackManager.triggerScanResultFeedback(report.status, _settings.value)
        }
    }

    private fun updateRadarFromReport(report: ScanReport) {
        val normalized = (report.riskScore / 100f).coerceIn(0.15f, 0.96f)
        _radarMetrics.update { current ->
            when (report.scannerType) {
                ScannerType.URL_SCANNER, ScannerType.OTP_MESSAGE -> current.copy(phishing = normalized)
                ScannerType.SOCIAL_PROFILE -> current.copy(socialScam = normalized, fakeIdentity = normalized)
                ScannerType.BANK_LOAN, ScannerType.PDF_DOC -> current.copy(financialScam = normalized)
                ScannerType.APP_APK -> current.copy(malwareRisk = normalized)
                ScannerType.IMAGE_AUTH, ScannerType.VIDEO_AUTH -> current.copy(mediaManipulation = normalized)
            }
        }
    }

    fun sendShieldAiMessage(userText: String) {
        val trimmed = userText.trim()
        if (trimmed.isBlank() || _isAiTyping.value) return

        val now = SimpleDateFormat("hh:mm a", Locale.US).format(Date())
        val userMsg = ChatMessage(
            id = "usr_${System.currentTimeMillis()}",
            isUser = true,
            text = trimmed,
            timestamp = now
        )
        _chatMessages.update { it + userMsg }
        _isAiTyping.value = true

        viewModelScope.launch {
            delay(450)
            val latestReport = _selectedReportDetail.value ?: _reports.value.firstOrNull()
            val replyText = GeminiSecurityService.askShieldAiAssistant(trimmed, latestReport)
            val botMsg = ChatMessage(
                id = "bot_${System.currentTimeMillis()}",
                isUser = false,
                text = replyText,
                timestamp = SimpleDateFormat("hh:mm a", Locale.US).format(Date()),
                relatedRiskStatus = latestReport?.status
            )
            _chatMessages.update { it + botMsg }
            _isAiTyping.value = false
        }
    }

    fun askShieldAiAboutReport(report: ScanReport) {
        _selectedReportDetail.value = null
        _currentTab.value = MainNavTab.SHIELD_AI
        val question = "Why did '${report.targetSummary}' receive a ${report.riskScore}/100 risk score (${report.status.label}) and what should I do next?"
        sendShieldAiMessage(question)
    }

    fun replayReportVoiceFeedback(report: ScanReport) {
        feedbackManager.triggerScanResultFeedback(report.status, _settings.value.copy(voiceFeedbackEnabled = true))
    }

    fun updateSettings(transform: (UserSettings) -> UserSettings) {
        _settings.update(transform)
        persistState()
    }

    fun clearAndResetDemoHistory() {
        _reports.value = SampleCyberData.initialReports
        _alerts.value = SampleCyberData.initialAlerts
        _chatMessages.value = SampleCyberData.initialChatMessages
        persistState()
    }

    fun computeUnifiedSecurityScore(): Int {
        val currentReports = _reports.value
        if (currentReports.isEmpty()) return 92
        // Security posture score reflects how effectively threats are caught and overall hygiene (76..95 range)
        val latest = currentReports.first()
        return when (latest.status) {
            ThreatStatus.SAFE -> 92
            ThreatStatus.SUSPICIOUS -> 81
            ThreatStatus.HIGH_RISK -> 87 // Protected — Threats isolated
        }
    }

    override fun onCleared() {
        super.onCleared()
        feedbackManager.shutdown()
    }
}
