package com.example.model

import kotlinx.serialization.Serializable

@Serializable
enum class ThreatStatus(val label: String, val badgeEmoji: String) {
    SAFE("SAFE", "🟢"),
    SUSPICIOUS("SUSPICIOUS", "🟡"),
    HIGH_RISK("THREAT DETECTED", "🔴")
}

@Serializable
enum class AlertSeverity(val label: String, val emoji: String) {
    CRITICAL("Critical", "🔴"),
    HIGH("High", "🟠"),
    MEDIUM("Medium", "🟡"),
    SAFE("Safe", "🟢")
}

@Serializable
enum class ScannerType(
    val id: String,
    val title: String,
    val shortTitle: String,
    val subtitle: String,
    val emoji: String,
    val badgeTag: String
) {
    URL_SCANNER(
        id = "url",
        title = "URL / Link Scanner",
        shortTitle = "URL Scan",
        subtitle = "Phishing, fake banking, payment & redirect analysis",
        emoji = "🔗",
        badgeTag = "WEB SHIELD"
    ),
    SOCIAL_PROFILE(
        id = "social",
        title = "Social Profile Scanner",
        shortTitle = "Social Scan",
        subtitle = "Impersonation, bot networks & scam account detection",
        emoji = "📱",
        badgeTag = "IDENTITY"
    ),
    OTP_MESSAGE(
        id = "otp",
        title = "OTP & Message Scanner",
        shortTitle = "OTP / SMS",
        subtitle = "Fake OTP traps, WhatsApp/SMS phishing & KYC urgency",
        emoji = "🔐",
        badgeTag = "SMS GUARD"
    ),
    BANK_LOAN(
        id = "bank",
        title = "Bank & Loan Scam Detector",
        shortTitle = "Bank & Loan",
        subtitle = "Predatory loans, fraudulent UPI/payment links & KYC fraud",
        emoji = "🏦",
        badgeTag = "FINANCE"
    ),
    IMAGE_AUTH(
        id = "image",
        title = "Image Authenticity Scanner",
        shortTitle = "Image AI",
        subtitle = "AI generation, pixel tampering, deepfake & EXIF forensics",
        emoji = "🖼️",
        badgeTag = "DEEPFAKE AI"
    ),
    VIDEO_AUTH(
        id = "video",
        title = "Video Authenticity Scanner",
        shortTitle = "Video AI",
        subtitle = "Frame-by-frame deepfake, lip-sync & synthetic voice scan",
        emoji = "🎥",
        badgeTag = "SYNTHETIC"
    ),
    APP_APK(
        id = "app",
        title = "App / APK Risk Scanner",
        shortTitle = "App Risk",
        subtitle = "Dangerous permissions, sideloaded APKs & spyware behavior",
        emoji = "📦",
        badgeTag = "APK AUDIT"
    ),
    PDF_DOC(
        id = "pdf",
        title = "PDF / Document Scanner",
        shortTitle = "Doc Scan",
        subtitle = "Fake bank notices, loan agreements & embedded phishing URLs",
        emoji = "📄",
        badgeTag = "DOC FORENSICS"
    )
}

@Serializable
data class VideoTimelineMarker(
    val timestampRange: String,
    val startFraction: Float,
    val endFraction: Float,
    val anomalyDescription: String,
    val confidencePercent: Int,
    val isHighRisk: Boolean = true
)

@Serializable
data class ScanMetadataItem(
    val key: String,
    val value: String,
    val isFlagged: Boolean = false
)

@Serializable
data class ScanReport(
    val id: String,
    val scannerType: ScannerType,
    val targetInput: String,
    val targetSummary: String,
    val timestamp: String,
    val riskScore: Int, // 0 to 100 (higher = more dangerous, except authenticity shown alongside)
    val status: ThreatStatus,
    val threatCategory: String,
    val headlineSummary: String,
    val detectionReasons: List<String>,
    val aiExplanation: String,
    val recommendedActions: List<String>,
    val metadataItems: List<ScanMetadataItem> = emptyList(),
    // Specialized fields per scanner type
    val domainName: String? = null,
    val httpsSecure: Boolean? = null,
    val domainReputation: String? = null,
    val platformName: String? = null,
    val authenticityScore: Int? = null, // 0-100 where 100 = 100% authentic
    val aiGeneratedProbability: Int? = null,
    val manipulationProbability: Int? = null,
    val deepfakeProbability: Int? = null,
    val confidenceLevel: String? = null,
    val videoTimelineMarkers: List<VideoTimelineMarker> = emptyList(),
    val suspiciousPhrases: List<String> = emptyList(),
    val flaggedPermissions: List<String> = emptyList(),
    val highlightedSections: List<String> = emptyList()
)

@Serializable
data class SecurityAlert(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: String,
    val severity: AlertSeverity,
    val scannerType: ScannerType,
    val sourceTarget: String,
    val actionTaken: String,
    val isRead: Boolean = false
)

@Serializable
data class ThreatRadarMetrics(
    val phishing: Float = 0.78f,
    val socialScam: Float = 0.62f,
    val financialScam: Float = 0.84f,
    val fakeIdentity: Float = 0.55f,
    val malwareRisk: Float = 0.42f,
    val mediaManipulation: Float = 0.68f
)

@Serializable
data class ChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val timestamp: String,
    val relatedRiskStatus: ThreatStatus? = null,
    val keyTakeaways: List<String> = emptyList()
)

@Serializable
data class UserSettings(
    val darkMode: Boolean = true,
    val voiceFeedbackEnabled: Boolean = true,
    val alertSoundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val realTimeAlertsEnabled: Boolean = true,
    val strictPrivacyMode: Boolean = true
)

data class InstalledAppCandidate(
    val appName: String,
    val packageName: String,
    val developer: String,
    val installSource: String,
    val permissions: List<String>,
    val suspiciousPermissions: List<String>,
    val presetRiskScore: Int,
    val isSystemOrTrusted: Boolean
)

data class DemoPresetItem(
    val label: String,
    val badge: String,
    val status: ThreatStatus,
    val inputPayload: String,
    val platformHint: String = ""
)
