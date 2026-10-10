package com.example.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import com.example.model.ThreatStatus
import com.example.model.UserSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class SecurityFeedbackManager(private val context: Context) {

    private var tts: TextToSpeech? = null
    private var isTtsReady = false
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        try {
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.US
                    tts?.setSpeechRate(1.02f)
                    isTtsReady = true
                }
            }
        } catch (_: Exception) {
            isTtsReady = false
        }
    }

    fun triggerScanResultFeedback(status: ThreatStatus, settings: UserSettings) {
        if (settings.vibrationEnabled) {
            triggerHaptics(status)
        }
        if (settings.alertSoundEnabled) {
            triggerSound(status)
        }
        if (settings.voiceFeedbackEnabled) {
            speakStatus(status)
        }
    }

    fun speakStatus(status: ThreatStatus) {
        val phrase = when (status) {
            ThreatStatus.SAFE -> "This content appears safe."
            ThreatStatus.SUSPICIOUS -> "Warning. Suspicious content detected."
            ThreatStatus.HIGH_RISK -> "Alert. High-risk threat detected. Avoid interacting with this content."
        }
        speakCustomText(phrase)
    }

    fun speakCustomText(text: String) {
        if (!isTtsReady) return
        try {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "scamshield_utterance_${System.currentTimeMillis()}")
        } catch (_: Exception) {
        }
    }

    private fun triggerHaptics(status: ThreatStatus) {
        try {
            val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                manager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (vibrator == null || !vibrator.hasVibrator()) return

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = when (status) {
                    ThreatStatus.SAFE -> VibrationEffect.createOneShot(65, VibrationEffect.DEFAULT_AMPLITUDE)
                    ThreatStatus.SUSPICIOUS -> VibrationEffect.createWaveform(longArrayOf(0, 110, 90, 140), -1)
                    ThreatStatus.HIGH_RISK -> VibrationEffect.createWaveform(longArrayOf(0, 220, 80, 220, 80, 340), -1)
                }
                vibrator.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                when (status) {
                    ThreatStatus.SAFE -> vibrator.vibrate(65)
                    ThreatStatus.SUSPICIOUS -> vibrator.vibrate(longArrayOf(0, 110, 90, 140), -1)
                    ThreatStatus.HIGH_RISK -> vibrator.vibrate(longArrayOf(0, 220, 80, 220, 80, 340), -1)
                }
            }
        } catch (_: Exception) {
        }
    }

    private fun triggerSound(status: ThreatStatus) {
        scope.launch {
            try {
                val toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 85)
                when (status) {
                    ThreatStatus.SAFE -> {
                        toneGen.startTone(ToneGenerator.TONE_PROP_ACK, 180)
                        delay(220)
                    }
                    ThreatStatus.SUSPICIOUS -> {
                        toneGen.startTone(ToneGenerator.TONE_PROP_BEEP2, 240)
                        delay(280)
                    }
                    ThreatStatus.HIGH_RISK -> {
                        toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 320)
                        delay(360)
                        toneGen.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 360)
                        delay(400)
                    }
                }
                toneGen.release()
            } catch (_: Exception) {
            }
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {
        }
    }
}
