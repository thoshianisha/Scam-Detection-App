package com.example.model

object SampleCyberData {

    val initialReports: List<ScanReport> = listOf(
        ScanReport(
            id = "rep_001",
            scannerType = ScannerType.URL_SCANNER,
            targetInput = "http://hdfc-kyc-instant-verify.top/login-update",
            targetSummary = "hdfc-kyc-instant-verify.top",
            timestamp = "Today, 10:42 AM",
            riskScore = 94,
            status = ThreatStatus.HIGH_RISK,
            threatCategory = "Phishing / Fraudulent Banking Link",
            headlineSummary = "Possible phishing / fraudulent banking link detected.",
            detectionReasons = listOf(
                "Typosquatting & brand impersonation of major banking institution ('hdfc-kyc')",
                "High-risk top-level domain (.top) registered less than 48 hours ago",
                "Unencrypted HTTP protocol lacking valid TLS/SSL banking certificate",
                "Contains credential harvesting form targeting NetBanking Customer ID & OTP"
            ),
            aiExplanation = "Shield AI detected a classic credential-harvesting phishing domain designed to mimic an official bank KYC portal. Legitimate banks never host account verification forms on '.top' domains or over unencrypted HTTP.",
            recommendedActions = listOf(
                "Do not open this link or enter any banking credentials.",
                "Close the browser tab immediately and clear autofill data.",
                "Verify your account status only through your bank's official mobile app."
            ),
            metadataItems = listOf(
                ScanMetadataItem("Domain", "hdfc-kyc-instant-verify.top", isFlagged = true),
                ScanMetadataItem("HTTPS / SSL", "Missing (Unencrypted HTTP)", isFlagged = true),
                ScanMetadataItem("Domain Age", "2 days old (Newly Registered)", isFlagged = true),
                ScanMetadataItem("Redirect Chain", "bit.ly -> hdfc-kyc-instant-verify.top", isFlagged = true),
                ScanMetadataItem("Domain Reputation", "06/100 (Blacklisted Phishing)", isFlagged = true)
            ),
            domainName = "hdfc-kyc-instant-verify.top",
            httpsSecure = false,
            domainReputation = "Critical Risk (Blacklisted)"
        ),
        ScanReport(
            id = "rep_002",
            scannerType = ScannerType.OTP_MESSAGE,
            targetInput = "URGENT: Your SBI YONO account will be suspended today due to incomplete PAN KYC. Share the 6-digit verification code sent to your number or click http://sbi-pan-update.xyz immediately.",
            targetSummary = "URGENT: Your SBI YONO account will be suspended...",
            timestamp = "Today, 09:58 AM",
            riskScore = 91,
            status = ThreatStatus.HIGH_RISK,
            threatCategory = "Bank KYC Impersonation & OTP Trap",
            headlineSummary = "High-risk fake KYC suspension message & OTP theft attempt.",
            detectionReasons = listOf(
                "Extreme urgency manipulation ('suspended today', 'immediately')",
                "Direct solicitation of 6-digit OTP verification code",
                "Embedded deceptive link ('sbi-pan-update.xyz') outside official banking domain",
                "Sender header originates from personal mobile number instead of regulated bank short-code"
            ),
            aiExplanation = "Scammers use fear of immediate account freeze to trick victims into handing over OTPs or clicking rogue links. No bank ever asks you to share an OTP or update KYC via an external '.xyz' link.",
            recommendedActions = listOf(
                "Never share any OTP, PIN, or password with anyone.",
                "Do not tap the embedded '.xyz' link inside the SMS.",
                "Report the sender number to your telecom spam registry (1909)."
            ),
            metadataItems = listOf(
                ScanMetadataItem("Threat Pattern", "KYC Freeze + OTP Solicitation", isFlagged = true),
                ScanMetadataItem("Urgency Index", "96% Psychological Pressure", isFlagged = true),
                ScanMetadataItem("Embedded URL", "http://sbi-pan-update.xyz", isFlagged = true)
            ),
            suspiciousPhrases = listOf(
                "suspended today",
                "Share the 6-digit verification code",
                "http://sbi-pan-update.xyz",
                "immediately"
            )
        ),
        ScanReport(
            id = "rep_003",
            scannerType = ScannerType.VIDEO_AUTH,
            targetInput = "ceo_crypto_double_giveaway_stream.mp4",
            targetSummary = "ceo_crypto_double_giveaway_stream.mp4",
            timestamp = "Today, 09:15 AM",
            riskScore = 88,
            status = ThreatStatus.HIGH_RISK,
            threatCategory = "AI Deepfake Face & Voice Clone",
            headlineSummary = "Synthetic GAN lip-sync and cloned voice frequencies detected.",
            detectionReasons = listOf(
                "Facial boundary blending artifacts around jawline between 00:04 and 00:09",
                "Unnatural eye-blink cadence (0 blinks over 22-second speech segment)",
                "Audio spectrogram reveals synthetic TTS pitch flattening at 14.2 kHz",
                "Promotes fraudulent cryptocurrency doubling QR code overlay"
            ),
            aiExplanation = "Frame-by-frame forensic evaluation indicates an 89% probability of GAN-based lip-sync replacement combined with an AI voice clone. Please note: AI authenticity detection is a probabilistic assessment and can produce false positives or negatives.",
            recommendedActions = listOf(
                "Do not scan the QR code or send cryptocurrency to the displayed wallet.",
                "Cross-check the announcement on the speaker's verified official channel.",
                "Report the livestream as synthetic impersonation fraud."
            ),
            metadataItems = listOf(
                ScanMetadataItem("Deepfake Probability", "89% Synthetic Media", isFlagged = true),
                ScanMetadataItem("Confidence Level", "High (92% Forensic Match)", isFlagged = false),
                ScanMetadataItem("Audio Stream", "Cloned Neural Vocoder Signature", isFlagged = true),
                ScanMetadataItem("Frame Rate", "29.97 fps (Re-encoded)", isFlagged = false)
            ),
            deepfakeProbability = 89,
            confidenceLevel = "High (92%)",
            videoTimelineMarkers = listOf(
                VideoTimelineMarker("00:04 - 00:09", 0.15f, 0.35f, "Jawline & mouth GAN blending mismatch", 91, true),
                VideoTimelineMarker("00:12 - 00:17", 0.48f, 0.68f, "Synthetic audio vocoder frequency spike", 87, true),
                VideoTimelineMarker("00:20 - 00:24", 0.80f, 0.95f, "Temporal frame flicker on head turn", 79, false)
            )
        ),
        ScanReport(
            id = "rep_004",
            scannerType = ScannerType.SOCIAL_PROFILE,
            targetInput = "@official_support_helpdesk_24x7 (Instagram)",
            targetSummary = "@official_support_helpdesk_24x7",
            timestamp = "Yesterday, 07:30 PM",
            riskScore = 67,
            status = ThreatStatus.SUSPICIOUS,
            threatCategory = "Customer Support Impersonation",
            headlineSummary = "Suspicious profile behavior & unverified support impersonation.",
            detectionReasons = listOf(
                "Account created 11 days ago claiming to be 'Official 24x7 Helpdesk'",
                "Abnormal follower-to-following ratio (42 followers / 1,890 following)",
                "Bio directs users to an external Telegram/WhatsApp link for refund processing",
                "Uses stock corporate logo without platform verification badge"
            ),
            aiExplanation = "Fraudulent customer support accounts monitor public complaints and reply asking users to DM them or fill out refund forms. This account exhibits bot-like following patterns and recent creation date.",
            recommendedActions = listOf(
                "Do not share banking details or remote-access codes via DM.",
                "Contact support strictly through the company's official in-app help center.",
                "Report the profile for brand impersonation."
            ),
            metadataItems = listOf(
                ScanMetadataItem("Platform", "Instagram", isFlagged = false),
                ScanMetadataItem("Account Age", "11 Days (High Risk)", isFlagged = true),
                ScanMetadataItem("Followers / Following", "42 / 1,890 (Ratio Anomaly)", isFlagged = true),
                ScanMetadataItem("Bio External Link", "t.me/instant_refund_desk", isFlagged = true)
            ),
            platformName = "Instagram"
        ),
        ScanReport(
            id = "rep_005",
            scannerType = ScannerType.APP_APK,
            targetInput = "QuickCash Instant Loan 5-Min APK (com.quickcash.instant.loan.apk)",
            targetSummary = "QuickCash Instant Loan APK",
            timestamp = "Yesterday, 04:12 PM",
            riskScore = 86,
            status = ThreatStatus.HIGH_RISK,
            threatCategory = "Predatory Loan App / Spyware Permissions",
            headlineSummary = "Excessive invasive permissions & sideloaded APK source.",
            detectionReasons = listOf(
                "Installed via sideloaded browser APK outside official app store",
                "Requests READ_CONTACTS, READ_SMS, and READ_CALL_LOG unrelated to core UI",
                "Requests SYSTEM_ALERT_WINDOW (screen overlay capability used for credential harvesting)",
                "Unverified developer certificate (Self-signed debug key)"
            ),
            aiExplanation = "Predatory loan apps frequently harvest contact lists and SMS inbox messages to extort borrowers or intercept banking OTPs. While permission analysis alone does not prove malicious code execution, the combination of sideloaded source and SMS/Overlay permissions poses severe privacy risk.",
            recommendedActions = listOf(
                "Revoke SMS, Contacts, and Draw-Over-Other-Apps permissions immediately.",
                "Uninstall sideloaded financial APKs from unverified lenders.",
                "Run Google Play Protect scan on your device."
            ),
            metadataItems = listOf(
                ScanMetadataItem("Package Name", "com.quickcash.instant.loan.apk", isFlagged = true),
                ScanMetadataItem("Install Source", "Sideloaded (Chrome Download)", isFlagged = true),
                ScanMetadataItem("Developer Cert", "Unverified / Self-Signed", isFlagged = true),
                ScanMetadataItem("High-Risk Perms", "4 Critical Permissions", isFlagged = true)
            ),
            flaggedPermissions = listOf(
                "android.permission.READ_SMS (Intercepts OTPs & messages)",
                "android.permission.READ_CONTACTS (Harvests contact list)",
                "android.permission.SYSTEM_ALERT_WINDOW (Screen overlay attack risk)",
                "android.permission.READ_MEDIA_IMAGES (Accesses private gallery)"
            )
        ),
        ScanReport(
            id = "rep_006",
            scannerType = ScannerType.URL_SCANNER,
            targetInput = "https://developer.android.com/security/best-practices",
            targetSummary = "developer.android.com",
            timestamp = "Yesterday, 02:05 PM",
            riskScore = 4,
            status = ThreatStatus.SAFE,
            threatCategory = "Verified Official Domain",
            headlineSummary = "Verified HTTPS domain with clean security reputation.",
            detectionReasons = listOf(
                "Valid TLS 1.3 certificate issued by Google Trust Services",
                "Established high-reputation domain with zero phishing reports",
                "No deceptive redirects, obfuscated scripts, or credential traps found"
            ),
            aiExplanation = "This URL belongs to a verified official domain (`developer.android.com`) using strong HTTPS encryption and clean DNS records. It is safe to browse.",
            recommendedActions = listOf(
                "This content appears safe to access.",
                "Continue following standard browser security hygiene."
            ),
            metadataItems = listOf(
                ScanMetadataItem("Domain", "developer.android.com", isFlagged = false),
                ScanMetadataItem("HTTPS / SSL", "Valid TLS 1.3 (Google Trust)", isFlagged = false),
                ScanMetadataItem("Domain Reputation", "99/100 (Trusted Authority)", isFlagged = false),
                ScanMetadataItem("Redirect Chain", "Direct (0 Redirects)", isFlagged = false)
            ),
            domainName = "developer.android.com",
            httpsSecure = true,
            domainReputation = "Trusted (99/100)"
        )
    )

    val initialAlerts: List<SecurityAlert> = listOf(
        SecurityAlert(
            id = "alt_001",
            title = "Potential phishing URL detected",
            description = "Domain 'hdfc-kyc-instant-verify.top' mimics a banking portal over unencrypted HTTP.",
            timestamp = "12 mins ago",
            severity = AlertSeverity.CRITICAL,
            scannerType = ScannerType.URL_SCANNER,
            sourceTarget = "http://hdfc-kyc-instant-verify.top/login-update",
            actionTaken = "Blocked & Flagged as Phishing"
        ),
        SecurityAlert(
            id = "alt_002",
            title = "Possible scam message detected",
            description = "SMS claiming immediate SBI YONO suspension requests 6-digit OTP & links to '.xyz' domain.",
            timestamp = "54 mins ago",
            severity = AlertSeverity.CRITICAL,
            scannerType = ScannerType.OTP_MESSAGE,
            sourceTarget = "SMS: URGENT SBI YONO PAN KYC",
            actionTaken = "Quarantined & User Warned"
        ),
        SecurityAlert(
            id = "alt_003",
            title = "Synthetic deepfake video indicators found",
            description = "89% deepfake probability detected in 'ceo_crypto_double_giveaway_stream.mp4' (GAN lip-sync & voice clone).",
            timestamp = "2 hours ago",
            severity = AlertSeverity.HIGH,
            scannerType = ScannerType.VIDEO_AUTH,
            sourceTarget = "ceo_crypto_double_giveaway_stream.mp4",
            actionTaken = "Forensic Timeline Generated"
        ),
        SecurityAlert(
            id = "alt_004",
            title = "Suspicious impersonation profile found",
            description = "@official_support_helpdesk_24x7 created 11 days ago routes users to external Telegram refund desk.",
            timestamp = "Yesterday",
            severity = AlertSeverity.MEDIUM,
            scannerType = ScannerType.SOCIAL_PROFILE,
            sourceTarget = "@official_support_helpdesk_24x7",
            actionTaken = "Flagged as Impersonation Risk"
        ),
        SecurityAlert(
            id = "alt_005",
            title = "High-risk sideloaded APK permissions",
            description = "QuickCash Instant Loan APK requests READ_SMS, READ_CONTACTS, and screen overlay permissions.",
            timestamp = "Yesterday",
            severity = AlertSeverity.HIGH,
            scannerType = ScannerType.APP_APK,
            sourceTarget = "com.quickcash.instant.loan.apk",
            actionTaken = "Permission Audit Alert"
        ),
        SecurityAlert(
            id = "alt_006",
            title = "Image authenticity could not be verified",
            description = "EXIF metadata stripped and Diffusion noise signature detected in uploaded payment screenshot.",
            timestamp = "2 days ago",
            severity = AlertSeverity.MEDIUM,
            scannerType = ScannerType.IMAGE_AUTH,
            sourceTarget = "wire_transfer_receipt_edited.jpg",
            actionTaken = "Marked as Manipulated Media"
        ),
        SecurityAlert(
            id = "alt_007",
            title = "Verified safe domain scan completed",
            description = "https://developer.android.com/security/best-practices passed TLS 1.3 & DNS reputation checks.",
            timestamp = "2 days ago",
            severity = AlertSeverity.SAFE,
            scannerType = ScannerType.URL_SCANNER,
            sourceTarget = "developer.android.com",
            actionTaken = "Verified Safe"
        )
    )

    val demoPresetsByScanner: Map<ScannerType, List<DemoPresetItem>> = mapOf(
        ScannerType.URL_SCANNER to listOf(
            DemoPresetItem(
                label = "Phishing Bank KYC Link",
                badge = "🔴 High Risk",
                status = ThreatStatus.HIGH_RISK,
                inputPayload = "http://hdfc-kyc-instant-verify.top/login-update"
            ),
            DemoPresetItem(
                label = "Shortened Parcel Scam URL",
                badge = "🟡 Suspicious",
                status = ThreatStatus.SUSPICIOUS,
                inputPayload = "https://bit.ly/3xYz9-customs-delivery-fee-pay"
            ),
            DemoPresetItem(
                label = "Official Android Security Docs",
                badge = "🟢 Safe",
                status = ThreatStatus.SAFE,
                inputPayload = "https://developer.android.com/security/best-practices"
            )
        ),
        ScannerType.SOCIAL_PROFILE to listOf(
            DemoPresetItem(
                label = "Fake Crypto Giveaway Bot",
                badge = "🔴 High Risk",
                status = ThreatStatus.HIGH_RISK,
                inputPayload = "https://x.com/elon_tesla_eth_drop_2026 | Created 3 days ago | Bio: Send 0.5 ETH get 2.0 ETH back instantly!",
                platformHint = "X"
            ),
            DemoPresetItem(
                label = "Support Desk Impersonator",
                badge = "🟡 Suspicious",
                status = ThreatStatus.SUSPICIOUS,
                inputPayload = "@official_support_helpdesk_24x7 | 42 followers, 1890 following | DM for instant bank refund via Telegram",
                platformHint = "Instagram"
            ),
            DemoPresetItem(
                label = "Verified Tech Professional",
                badge = "🟢 Safe",
                status = ThreatStatus.SAFE,
                inputPayload = "https://linkedin.com/in/arjun-mehta-cybersec | 8+ yrs account age | 2,400 connections | Verified workplace",
                platformHint = "LinkedIn"
            )
        ),
        ScannerType.OTP_MESSAGE to listOf(
            DemoPresetItem(
                label = "Fake Bank KYC + OTP Trap",
                badge = "🔴 Scam",
                status = ThreatStatus.HIGH_RISK,
                inputPayload = "URGENT: Your SBI YONO account will be suspended today due to incomplete PAN KYC. Share the 6-digit verification code sent to your number or click http://sbi-pan-update.xyz immediately."
            ),
            DemoPresetItem(
                label = "WhatsApp Part-Time Job Offer",
                badge = "🟡 Suspicious",
                status = ThreatStatus.SUSPICIOUS,
                inputPayload = "Congrats! Your profile was shortlisted for Google Maps hotel rating remote job. Earn Rs 4,500-9,000 daily from phone. Contact receptionist on Telegram: https://t.me/vip_task_bonus88"
            ),
            DemoPresetItem(
                label = "Legitimate Delivery Update",
                badge = "🟢 Safe",
                status = ThreatStatus.SAFE,
                inputPayload = "Your order #408-99124 is out for delivery today by 6 PM. Track your package directly inside your official shopping app. No OTP is required for prepaid delivery."
            )
        ),
        ScannerType.BANK_LOAN to listOf(
            DemoPresetItem(
                label = "0% Interest Instant Loan Trap",
                badge = "🔴 High Risk",
                status = ThreatStatus.HIGH_RISK,
                inputPayload = "PRE-APPROVED Rs 5,00,000 Instant Personal Loan in 3 minutes! No CIBIL check, 0% interest. Pay Rs 1,999 processing fee via UPI link http://instant-mudra-approve.in/pay to disburse now!"
            ),
            DemoPresetItem(
                label = "Unverified Credit Card Limit Upgrade",
                badge = "🟡 Suspicious",
                status = ThreatStatus.SUSPICIOUS,
                inputPayload = "Dear Customer, your credit card limit is eligible for a Rs 3.5L free increase. Confirm your card CVV and expiry date at http://cc-reward-upgrade-portal.net to activate."
            ),
            DemoPresetItem(
                label = "Standard Monthly Bank Alert",
                badge = "🟢 Safe",
                status = ThreatStatus.SAFE,
                inputPayload = "Your monthly e-statement for Account XX4821 has been generated and sent to your registered email address. Never share your OTP, PIN, or CVV with bank representatives."
            )
        ),
        ScannerType.IMAGE_AUTH to listOf(
            DemoPresetItem(
                label = "AI-Generated CEO Deepfake Portrait",
                badge = "🔴 94% AI Fake",
                status = ThreatStatus.HIGH_RISK,
                inputPayload = "demo_image://ai_deepfake_ceo_portrait.png"
            ),
            DemoPresetItem(
                label = "Edited UPI Payment Receipt Screenshot",
                badge = "🟡 72% Tampered",
                status = ThreatStatus.SUSPICIOUS,
                inputPayload = "demo_image://manipulated_upi_receipt.jpg"
            ),
            DemoPresetItem(
                label = "Original Camera Photo (Valid EXIF)",
                badge = "🟢 97% Authentic",
                status = ThreatStatus.SAFE,
                inputPayload = "demo_image://authentic_camera_photo_exif.jpg"
            )
        ),
        ScannerType.VIDEO_AUTH to listOf(
            DemoPresetItem(
                label = "Deepfake Crypto Giveaway Stream",
                badge = "🔴 89% Deepfake",
                status = ThreatStatus.HIGH_RISK,
                inputPayload = "demo_video://ceo_crypto_double_giveaway_stream.mp4"
            ),
            DemoPresetItem(
                label = "Voice-Cloned Emergency Wire Request",
                badge = "🟡 68% Synthetic",
                status = ThreatStatus.SUSPICIOUS,
                inputPayload = "demo_video://cfo_urgent_wire_voice_clone.mp4"
            ),
            DemoPresetItem(
                label = "Authentic Press Briefing Recording",
                badge = "🟢 96% Authentic",
                status = ThreatStatus.SAFE,
                inputPayload = "demo_video://verified_press_briefing_1080p.mp4"
            )
        ),
        ScannerType.APP_APK to listOf(
            DemoPresetItem(
                label = "QuickCash Instant Loan APK (Sideloaded)",
                badge = "🔴 High Risk",
                status = ThreatStatus.HIGH_RISK,
                inputPayload = "com.quickcash.instant.loan.apk"
            ),
            DemoPresetItem(
                label = "SuperBright Flashlight Pro (Adware/SMS)",
                badge = "🟡 Suspicious",
                status = ThreatStatus.SUSPICIOUS,
                inputPayload = "com.free.superbright.flashlight.led"
            ),
            DemoPresetItem(
                label = "ScamShield AI (Official Security Suite)",
                badge = "🟢 Low Risk",
                status = ThreatStatus.SAFE,
                inputPayload = "com.aistudio.scamshieldai.xkrpvn"
            )
        ),
        ScannerType.PDF_DOC to listOf(
            DemoPresetItem(
                label = "Urgent_RBI_Account_Freeze_Notice.pdf",
                badge = "🔴 High Risk",
                status = ThreatStatus.HIGH_RISK,
                inputPayload = "Urgent_RBI_Account_Freeze_Notice.pdf | Content: 'Reserve Bank Cyber Cell Notice: Your savings account is frozen for tax non-compliance. Remit Rs 14,500 clearance bond to UPI ID rbi.legal.clearance@okaxis or click http://rbi-verify-portal.org/release within 2 hours.'"
            ),
            DemoPresetItem(
                label = "ZeroInterest_Loan_Sanction_Letter.pdf",
                badge = "🟡 Suspicious",
                status = ThreatStatus.SUSPICIOUS,
                inputPayload = "ZeroInterest_Loan_Sanction_Letter.pdf | Content: 'Sanctioned Amount: Rs 8,00,000 at 0.5% p.a. Mandatory insurance deposit of Rs 3,250 required prior to NEFT disbursement. Contact WhatsApp +91-98000-00112.'"
            ),
            DemoPresetItem(
                label = "Standard_Cloud_Hosting_Invoice_Q3.pdf",
                badge = "🟢 Safe",
                status = ThreatStatus.SAFE,
                inputPayload = "Standard_Cloud_Hosting_Invoice_Q3.pdf | Content: 'Tax Invoice #INV-2026-0891. Billed to Enterprise Account. Payment processed automatically via registered corporate card ending 4412. No manual wire transfer required.'"
            )
        )
    )

    val demoInstalledApps: List<InstalledAppCandidate> = listOf(
        InstalledAppCandidate(
            appName = "QuickCash Instant Loan 5-Min",
            packageName = "com.quickcash.instant.loan.apk",
            developer = "Unknown Unverified Publisher (Self-Signed)",
            installSource = "Sideloaded APK (Browser Download)",
            permissions = listOf(
                "android.permission.READ_SMS",
                "android.permission.RECEIVE_SMS",
                "android.permission.READ_CONTACTS",
                "android.permission.READ_CALL_LOG",
                "android.permission.SYSTEM_ALERT_WINDOW",
                "android.permission.READ_MEDIA_IMAGES",
                "android.permission.INTERNET"
            ),
            suspiciousPermissions = listOf(
                "READ_SMS (Can intercept banking OTPs)",
                "READ_CONTACTS (Can harvest address book for extortion)",
                "SYSTEM_ALERT_WINDOW (Can draw fake login overlays)",
                "READ_CALL_LOG (Tracks personal call history)"
            ),
            presetRiskScore = 88,
            isSystemOrTrusted = false
        ),
        InstalledAppCandidate(
            appName = "SuperBright Flashlight Pro",
            packageName = "com.free.superbright.flashlight.led",
            developer = "FastUtility Studio Ltd",
            installSource = "Third-Party APK Mirror",
            permissions = listOf(
                "android.permission.CAMERA",
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.READ_CONTACTS",
                "android.permission.REQUEST_INSTALL_PACKAGES",
                "android.permission.INTERNET"
            ),
            suspiciousPermissions = listOf(
                "ACCESS_FINE_LOCATION (Unnecessary for a flashlight app)",
                "READ_CONTACTS (Unnecessary for a flashlight app)",
                "REQUEST_INSTALL_PACKAGES (Can prompt silent APK installs)"
            ),
            presetRiskScore = 64,
            isSystemOrTrusted = false
        ),
        InstalledAppCandidate(
            appName = "ScreenShare Remote Assist",
            packageName = "com.remote.anydesk.assist.clone",
            developer = "QuickSupport Helper Corp",
            installSource = "Sideloaded via WhatsApp Link",
            permissions = listOf(
                "android.permission.BIND_ACCESSIBILITY_SERVICE",
                "android.permission.SYSTEM_ALERT_WINDOW",
                "android.permission.RECORD_AUDIO",
                "android.permission.INTERNET"
            ),
            suspiciousPermissions = listOf(
                "BIND_ACCESSIBILITY_SERVICE (Full screen reading & remote control)",
                "SYSTEM_ALERT_WINDOW (Can obscure screen during bank transfers)"
            ),
            presetRiskScore = 82,
            isSystemOrTrusted = false
        ),
        InstalledAppCandidate(
            appName = "ScamShield AI",
            packageName = "com.aistudio.scamshieldai.xkrpvn",
            developer = "ScamShield Cyber Defense Labs",
            installSource = "Verified Official Build",
            permissions = listOf(
                "android.permission.INTERNET",
                "android.permission.VIBRATE"
            ),
            suspiciousPermissions = emptyList(),
            presetRiskScore = 4,
            isSystemOrTrusted = true
        )
    )

    val initialChatMessages: List<ChatMessage> = listOf(
        ChatMessage(
            id = "msg_welcome",
            isUser = false,
            text = "Hello! I am **Shield AI**, your personal cyber defense analyst. You can ask me to explain any scan result, analyze a suspicious SMS/loan offer, inspect a URL, or guide you on emergency recovery steps if you clicked a malicious link.",
            timestamp = "Just now",
            relatedRiskStatus = ThreatStatus.SAFE,
            keyTakeaways = listOf(
                "Ask: 'Is this message a scam?'",
                "Ask: 'Why is this URL dangerous?'",
                "Ask: 'What should I do next after a threat alert?'"
            )
        )
    )
}
