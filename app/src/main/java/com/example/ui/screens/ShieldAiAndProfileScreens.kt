package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.ChatMessage
import com.example.model.ScanReport
import com.example.model.ThreatStatus
import com.example.model.UserSettings
import com.example.ui.components.GlassmorphicCyberCard
import com.example.ui.components.PrivacyFirstNoticeCard
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SafeEmerald
import com.example.ui.theme.SuspiciousAmber
import com.example.ui.theme.ThreatCrimson

@Composable
fun ShieldAiScreen(
    messages: List<ChatMessage>,
    isAiTyping: Boolean,
    latestReport: ScanReport?,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var draftText by remember { mutableStateOf("") }

    val quickPrompts = listOf(
        "Is this message a scam?",
        "Is this loan offer suspicious?",
        "Why is this URL dangerous?",
        "What should I do next?",
        "Why did this profile receive a high risk score?"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("shield_ai_screen")
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = NeonPurple.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, NeonPurple)
                ) {
                    Icon(
                        imageVector = Icons.Filled.SmartToy,
                        contentDescription = "Shield AI",
                        tint = NeonPurple,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Shield AI Security Assistant",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Explainable AI Threat Analyst • Online",
                        style = MaterialTheme.typography.bodySmall,
                        color = SafeEmerald
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Active Scan Context Pill
        if (latestReport != null) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CyberCyan.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Active Context: ${latestReport.scannerType.shortTitle} (${latestReport.riskScore}/100 Risk)",
                            style = MaterialTheme.typography.labelMedium,
                            color = CyberCyan
                        )
                        Text(
                            text = latestReport.targetSummary,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                    Text(
                        text = "Explain →",
                        style = MaterialTheme.typography.labelLarge,
                        color = NeonPurple,
                        modifier = Modifier.clickable {
                            onSendMessage("Explain why '${latestReport.targetSummary}' got a ${latestReport.riskScore}/100 risk score and what I should do next.")
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Quick-Ask Prompts Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(quickPrompts) { prompt ->
                Surface(
                    shape = RoundedCornerShape(50),
                    color = NeonPurple.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, NeonPurple.copy(alpha = 0.45f)),
                    modifier = Modifier.clickable { onSendMessage(prompt) }
                ) {
                    Text(
                        text = prompt,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Chat Message List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 6.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatBubbleItem(message = msg)
            }

            if (isAiTyping) {
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, NeonPurple.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = CyberCyan
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Shield AI is analyzing threat vectors...",
                                style = MaterialTheme.typography.bodySmall,
                                color = CyberCyan
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Input Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = draftText,
                onValueChange = { draftText = it },
                modifier = Modifier
                    .weight(1f)
                    .testTag("shield_ai_input"),
                placeholder = { Text("Ask Shield AI about any link, SMS, or scam...") },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = CircleShape,
                color = ElectricBlue
            ) {
                IconButton(
                    onClick = {
                        if (draftText.isNotBlank()) {
                            onSendMessage(draftText)
                            draftText = ""
                        }
                    },
                    modifier = Modifier.testTag("shield_ai_send_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send to Shield AI",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBubbleItem(message: ChatMessage) {
    val isUser = message.isUser
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (isUser) 18.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 18.dp
            ),
            color = if (isUser) ElectricBlue else MaterialTheme.colorScheme.surfaceVariant,
            border = if (isUser) null else BorderStroke(1.dp, CyberCyan.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth(0.88f)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUser) "You" else "🤖 Shield AI",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isUser) Color.White.copy(alpha = 0.85f) else CyberCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = message.timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isUser) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = message.text.replace("**", ""),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun ProfileSettingsScreen(
    settings: UserSettings,
    totalScans: Int,
    threatsBlocked: Int,
    onUpdateSettings: ((UserSettings) -> UserSettings) -> Unit,
    onTestSensoryAlert: (ThreatStatus) -> Unit,
    onResetDemoHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_settings_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Cyber Profile Header Card
        item {
            GlassmorphicCyberCard(accentColor = CyberCyan) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = ElectricBlue.copy(alpha = 0.25f),
                            border = BorderStroke(2.dp, CyberCyan),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.Security,
                                    contentDescription = null,
                                    tint = CyberCyan,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ScamShield Cyber Guardian",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Personal Digital Risk Protection • Active",
                                style = MaterialTheme.typography.bodySmall,
                                color = SafeEmerald
                            )
                            Text(
                                text = "$totalScans Total Scans • $threatsBlocked Threats Neutralized",
                                style = MaterialTheme.typography.labelMedium,
                                color = CyberCyan
                            )
                        }
                    }
                }
            }
        }

        // Multi-Sensory Feedback Test Bench (Voice + Sound + Vibration)
        item {
            GlassmorphicCyberCard(accentColor = NeonPurple) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🔊 Test Multi-Sensory Security Feedback",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Preview the Voice Announcement, Buzzer Tone, and Haptic Vibration for each security state:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onTestSensoryAlert(ThreatStatus.SAFE) },
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, SafeEmerald),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text("🟢 Safe", color = SafeEmerald, style = MaterialTheme.typography.labelMedium)
                        }
                        OutlinedButton(
                            onClick = { onTestSensoryAlert(ThreatStatus.SUSPICIOUS) },
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, SuspiciousAmber),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text("🟡 Warn", color = SuspiciousAmber, style = MaterialTheme.typography.labelMedium)
                        }
                        OutlinedButton(
                            onClick = { onTestSensoryAlert(ThreatStatus.HIGH_RISK) },
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, ThreatCrimson),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text("🔴 Threat", color = ThreatCrimson, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }

        // Settings Switches Card
        item {
            GlassmorphicCyberCard(accentColor = CyberCyan) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Appearance & Sensory Feedback Settings",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = CyberCyan
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    SettingsSwitchRow(
                        icon = Icons.Filled.DarkMode,
                        title = "Premium Cyber Dark Mode",
                        subtitle = "Obsidian navy glassmorphism with neon security highlights",
                        checked = settings.darkMode,
                        onCheckedChange = { enabled ->
                            onUpdateSettings { it.copy(darkMode = enabled) }
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    SettingsSwitchRow(
                        icon = Icons.Filled.RecordVoiceOver,
                        title = "Voice Safety Feedback (TTS)",
                        subtitle = "Speaks 'This content appears safe' or 'Alert. High-risk threat detected'",
                        checked = settings.voiceFeedbackEnabled,
                        onCheckedChange = { enabled ->
                            onUpdateSettings { it.copy(voiceFeedbackEnabled = enabled) }
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    SettingsSwitchRow(
                        icon = Icons.Filled.VolumeUp,
                        title = "Security Alert Sound / Buzzer",
                        subtitle = "Plays warning buzzer for threats and soft chime for safe results",
                        checked = settings.alertSoundEnabled,
                        onCheckedChange = { enabled ->
                            onUpdateSettings { it.copy(alertSoundEnabled = enabled) }
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    SettingsSwitchRow(
                        icon = Icons.Filled.Vibration,
                        title = "Haptic Vibration Feedback",
                        subtitle = "Tactile pulse patterns coded to Safe, Suspicious, and Threat verdicts",
                        checked = settings.vibrationEnabled,
                        onCheckedChange = { enabled ->
                            onUpdateSettings { it.copy(vibrationEnabled = enabled) }
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    SettingsSwitchRow(
                        icon = Icons.Filled.Notifications,
                        title = "Real-Time Threat Notifications",
                        subtitle = "Log instant alerts to the Alert Center after every scan",
                        checked = settings.realTimeAlertsEnabled,
                        onCheckedChange = { enabled ->
                            onUpdateSettings { it.copy(realTimeAlertsEnabled = enabled) }
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    SettingsSwitchRow(
                        icon = Icons.Filled.Lock,
                        title = "Strict Privacy Protection",
                        subtitle = "Mask sensitive tokens and never store raw OTPs or passwords",
                        checked = settings.strictPrivacyMode,
                        onCheckedChange = { enabled ->
                            onUpdateSettings { it.copy(strictPrivacyMode = enabled) }
                        }
                    )
                }
            }
        }

        // Scan History Management & About ScamShield AI
        item {
            GlassmorphicCyberCard(accentColor = ElectricBlue) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "About ScamShield AI",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "ScamShield AI v2.4 Enterprise Edition is an AI-powered Digital Risk Protection and Personal Cyber Safety Platform. It combines multi-vector heuristics with Explainable AI to protect users from phishing links, fake social profiles, OTP fraud, predatory loan scams, deepfake media, and risky APKs.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = onResetDemoHistory,
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, CyberCyan)
                    ) {
                        Icon(imageVector = Icons.Filled.DeleteSweep, contentDescription = null, tint = CyberCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reset / Reload Hackathon Sample Scan History", color = CyberCyan)
                    }
                }
            }
        }

        item {
            PrivacyFirstNoticeCard()
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = CyberCyan,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = CyberCyan
            )
        )
    }
}
