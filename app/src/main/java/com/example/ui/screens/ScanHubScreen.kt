package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.DemoPresetItem
import com.example.model.InstalledAppCandidate
import com.example.model.SampleCyberData
import com.example.model.ScannerType
import com.example.model.ThreatStatus
import com.example.ui.components.GlassmorphicCyberCard
import com.example.ui.components.PrivacyFirstNoticeCard
import com.example.ui.components.statusColor
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SafeEmerald
import com.example.ui.theme.SuspiciousAmber
import com.example.ui.theme.ThreatCrimson

@Composable
fun ScanHubScreen(
    activeScanner: ScannerType?,
    isScanning: Boolean,
    scanProgress: Float,
    scanStepLabel: String,
    installedApps: List<InstalledAppCandidate>,
    onSelectScanner: (ScannerType) -> Unit,
    onCloseScanner: () -> Unit,
    onRunScan: (ScannerType, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (activeScanner != null) {
        BackHandler {
            if (!isScanning) onCloseScanner()
        }
        ScannerWorkspaceScreen(
            scanner = activeScanner,
            isScanning = isScanning,
            scanProgress = scanProgress,
            scanStepLabel = scanStepLabel,
            installedApps = installedApps,
            onBack = onCloseScanner,
            onRunScan = { payload, platform ->
                onRunScan(activeScanner, payload, platform)
            },
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .testTag("scan_hub_screen"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Radar,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Security Scanners Suite",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Select any AI-powered scanner below to inspect links, social profiles, messages, banking offers, media authenticity, APKs, or documents.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 2-column grid of the 8 Scanners
            val scannerPairs = ScannerType.entries.chunked(2)
            items(scannerPairs) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { scanner ->
                        ScannerHubBlockCard(
                            scanner = scanner,
                            onClick = { onSelectScanner(scanner) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item {
                PrivacyFirstNoticeCard()
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun ScannerHubBlockCard(
    scanner: ScannerType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardAccent = when (scanner) {
        ScannerType.URL_SCANNER -> CyberCyan
        ScannerType.SOCIAL_PROFILE -> NeonPurple
        ScannerType.OTP_MESSAGE -> SafeEmerald
        ScannerType.BANK_LOAN -> SuspiciousAmber
        ScannerType.IMAGE_AUTH -> ElectricBlue
        ScannerType.VIDEO_AUTH -> ThreatCrimson
        ScannerType.APP_APK -> CyberCyan
        ScannerType.PDF_DOC -> NeonPurple
    }

    GlassmorphicCyberCard(
        modifier = modifier
            .height(172.dp)
            .testTag("scanner_card_${scanner.id}"),
        accentColor = cardAccent,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = cardAccent.copy(alpha = 0.18f),
                    border = BorderStroke(1.dp, cardAccent.copy(alpha = 0.45f))
                ) {
                    Text(
                        text = scanner.emoji,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = cardAccent.copy(alpha = 0.14f)
                ) {
                    Text(
                        text = scanner.badgeTag,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = cardAccent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column {
                Text(
                    text = scanner.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = scanner.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Launch Scanner",
                    style = MaterialTheme.typography.labelMedium,
                    color = cardAccent
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = cardAccent,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScannerWorkspaceScreen(
    scanner: ScannerType,
    isScanning: Boolean,
    scanProgress: Float,
    scanStepLabel: String,
    installedApps: List<InstalledAppCandidate>,
    onBack: () -> Unit,
    onRunScan: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val presets = SampleCyberData.demoPresetsByScanner[scanner].orEmpty()
    var inputText by remember(scanner) {
        mutableStateOf(presets.firstOrNull()?.inputPayload.orEmpty())
    }
    var selectedPlatform by remember(scanner) {
        mutableStateOf(if (scanner == ScannerType.SOCIAL_PROFILE) "Instagram" else "")
    }
    var selectedMediaUri by remember(scanner) { mutableStateOf<Uri?>(null) }
    val clipboardManager = LocalClipboardManager.current

    // Zero-permission Photo Picker for Image Scanner
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedMediaUri = uri
            inputText = "gallery_image://${uri.lastPathSegment ?: "selected_photo.jpg"}"
        }
    }

    // Zero-permission Video Picker for Video Scanner
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedMediaUri = uri
            inputText = "gallery_video://${uri.lastPathSegment ?: "selected_video.mp4"}"
        }
    }

    // Document Picker for PDF Scanner
    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedMediaUri = uri
            inputText = "Uploaded_Document_${uri.lastPathSegment ?: "Notice.pdf"} | Verified PDF stream inspection"
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("scanner_workspace_${scanner.id}"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    enabled = !isScanning,
                    modifier = Modifier.testTag("scanner_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Scanners"
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = scanner.emoji,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = scanner.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = scanner.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Strict Privacy Warning for OTP / Bank / PDF Scanners
        if (scanner == ScannerType.OTP_MESSAGE || scanner == ScannerType.BANK_LOAN) {
            item {
                PrivacyFirstNoticeCard()
            }
        }

        // AI Disclaimer for Image / Video / App Scanners
        if (scanner == ScannerType.IMAGE_AUTH || scanner == ScannerType.VIDEO_AUTH || scanner == ScannerType.APP_APK) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = NeonPurple.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, NeonPurple.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = NeonPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (scanner) {
                                ScannerType.IMAGE_AUTH -> "Forensic Notice: Results represent an AI-based probabilistic assessment of pixel & EXIF patterns, not absolute proof."
                                ScannerType.VIDEO_AUTH -> "Forensic Notice: AI deepfake & voice-clone detection is probabilistic and can produce false positives or false negatives."
                                else -> "Audit Notice: App Risk Scanner evaluates permissions and install source. It never claims an app is malicious without sufficient evidence."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Hackathon Demo Presets Card (One-tap Safe / Suspicious / High-Risk scenarios)
        item {
            GlassmorphicCyberCard(accentColor = NeonPurple) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡ Instant Demo Scenarios (Tap to Load & Scan)",
                            style = MaterialTheme.typography.labelLarge,
                            color = CyberCyan
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        presets.forEachIndexed { idx, preset ->
                            DemoPresetRow(
                                preset = preset,
                                onSelectAndScan = {
                                    inputText = preset.inputPayload
                                    if (preset.platformHint.isNotBlank()) {
                                        selectedPlatform = preset.platformHint
                                    }
                                    onRunScan(preset.inputPayload, preset.platformHint.ifBlank { selectedPlatform })
                                },
                                modifier = Modifier.testTag("demo_preset_$idx")
                            )
                        }
                    }
                }
            }
        }

        // Platform Selector for Social Profile Scanner
        if (scanner == ScannerType.SOCIAL_PROFILE) {
            item {
                GlassmorphicCyberCard(accentColor = CyberCyan) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Select Social Platform",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("Instagram", "Facebook", "X", "LinkedIn", "Telegram").forEach { platform ->
                                FilterChip(
                                    selected = selectedPlatform == platform,
                                    onClick = { selectedPlatform = platform },
                                    label = { Text(platform) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ElectricBlue.copy(alpha = 0.28f),
                                        selectedLabelColor = CyberCyan
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Media / Document / App Picker Controls depending on ScannerType
        when (scanner) {
            ScannerType.IMAGE_AUTH -> {
                item {
                    GlassmorphicCyberCard(accentColor = ElectricBlue) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Image,
                                contentDescription = "Upload Image",
                                tint = CyberCyan,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Select Image from Phone Gallery",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Checks for Diffusion/GAN artifacts, pixel cloning, and EXIF tampering",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = {
                                    imagePickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                border = BorderStroke(1.dp, CyberCyan)
                            ) {
                                Icon(imageVector = Icons.Filled.UploadFile, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Choose Photo from Gallery")
                            }
                        }
                    }
                }
            }

            ScannerType.VIDEO_AUTH -> {
                item {
                    GlassmorphicCyberCard(accentColor = ThreatCrimson) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.VideoFile,
                                contentDescription = "Select Video",
                                tint = CyberCyan,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Select Video for Deepfake Analysis",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Extracts keyframes & audio spectrograms to flag synthetic face/voice segments",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = {
                                    videoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                    )
                                },
                                border = BorderStroke(1.dp, CyberCyan)
                            ) {
                                Icon(imageVector = Icons.Filled.PlayCircle, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Choose Video from Gallery")
                            }
                        }
                    }
                }
            }

            ScannerType.PDF_DOC -> {
                item {
                    GlassmorphicCyberCard(accentColor = NeonPurple) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Description,
                                contentDescription = "Upload PDF",
                                tint = CyberCyan,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Upload PDF or Document Notice",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Detects fake loan sanction letters, forged bank notices & embedded phishing links",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = {
                                    pdfPickerLauncher.launch(arrayOf("application/pdf", "text/*"))
                                },
                                border = BorderStroke(1.dp, CyberCyan)
                            ) {
                                Icon(imageVector = Icons.Filled.UploadFile, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Select PDF / Document")
                            }
                        }
                    }
                }
            }

            ScannerType.APP_APK -> {
                item {
                    GlassmorphicCyberCard(accentColor = CyberCyan) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Android,
                                    contentDescription = null,
                                    tint = SafeEmerald,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Select Installed App or APK Package to Audit",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                installedApps.forEach { app ->
                                    val badgeColor = when {
                                        app.presetRiskScore >= 70 -> ThreatCrimson
                                        app.presetRiskScore >= 35 -> SuspiciousAmber
                                        else -> SafeEmerald
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                                        border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.4f)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                inputText = "${app.appName} (${app.packageName})"
                                                onRunScan(inputText, app.installSource)
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = app.appName,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "${app.packageName} • ${app.installSource}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                if (app.suspiciousPermissions.isNotEmpty()) {
                                                    Text(
                                                        text = "Flags: ${app.suspiciousPermissions.size} sensitive permissions",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = badgeColor
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = badgeColor.copy(alpha = 0.16f)
                                            ) {
                                                Text(
                                                    text = "Audit →",
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = badgeColor,
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
            }

            else -> Unit
        }

        // Main Input / Target Box & Primary Scan Action Button
        item {
            GlassmorphicCyberCard(accentColor = CyberCyan) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (scanner) {
                                ScannerType.URL_SCANNER -> "Enter or Paste URL to Inspect"
                                ScannerType.SOCIAL_PROFILE -> "Profile Handle, URL, or Bio Snippet"
                                ScannerType.OTP_MESSAGE -> "Paste Suspicious SMS / WhatsApp Message"
                                ScannerType.BANK_LOAN -> "Paste Bank Alert, Loan Offer, or Payment Link"
                                ScannerType.IMAGE_AUTH -> "Target Image Resource / Descriptor"
                                ScannerType.VIDEO_AUTH -> "Target Video Stream / Descriptor"
                                ScannerType.APP_APK -> "Package Name or APK Identifier"
                                ScannerType.PDF_DOC -> "Document Content or File Identifier"
                            },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Paste",
                            style = MaterialTheme.typography.labelLarge,
                            color = CyberCyan,
                            modifier = Modifier.clickable {
                                val clip = clipboardManager.getText()?.text
                                if (!clip.isNullOrBlank()) {
                                    inputText = clip
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("scanner_input_field"),
                        minLines = if (scanner == ScannerType.OTP_MESSAGE || scanner == ScannerType.BANK_LOAN || scanner == ScannerType.PDF_DOC) 4 else 2,
                        maxLines = 6,
                        placeholder = {
                            Text("Paste link, message, username, or select a preset above...")
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Animated Cyber Scan Progress Indicator
                    AnimatedVisibility(visible = isScanning) {
                        CyberLaserScanningBanner(
                            progress = scanProgress,
                            stepLabel = scanStepLabel
                        )
                    }

                    Button(
                        onClick = { onRunScan(inputText, selectedPlatform) },
                        enabled = inputText.isNotBlank() && !isScanning,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("run_scan_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Security,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isScanning) "AI Forensic Scan in Progress..." else "Run Deep AI Security Scan",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DemoPresetRow(
    preset: DemoPresetItem,
    onSelectAndScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = statusColor(preset.status)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelectAndScan() },
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.45f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = preset.label,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = accent.copy(alpha = 0.16f)
                    ) {
                        Text(
                            text = preset.badge,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = accent,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = preset.inputPayload,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Test →",
                style = MaterialTheme.typography.labelLarge,
                color = CyberCyan
            )
        }
    }
}

@Composable
private fun CyberLaserScanningBanner(
    progress: Float,
    stepLabel: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "laser_scan")
    val alphaGlow by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(550, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha_glow"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp),
        shape = RoundedCornerShape(14.dp),
        color = CyberCyan.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, CyberCyan.copy(alpha = alphaGlow))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "NEURAL THREAT ENGINE ACTIVE",
                    style = MaterialTheme.typography.labelMedium,
                    color = CyberCyan,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = CyberCyan
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stepLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50)),
                color = CyberCyan,
                trackColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}
