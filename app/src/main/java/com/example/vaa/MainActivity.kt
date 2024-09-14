package com.example.vaa

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.ImageAnalysis
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.vaa.data.TfLiteClassifier
import com.example.vaa.domain.Detection
import com.example.vaa.presentation.CameraPreview
import com.example.vaa.presentation.ImageAnalyzer
import com.example.vaa.presentation.VoiceSettingsScreen
import com.example.vaa.util.FlashlightController
import com.example.vaa.util.TutorialManager
import com.example.vaa.util.VibrationHelper
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var textToSpeech: TextToSpeech
    private var speechRate by mutableStateOf(1.0f)
    private var pitch by mutableStateOf(1.0f)
    private lateinit var vibrationHelper: VibrationHelper
    private lateinit var tutorialManager: TutorialManager
    private lateinit var flashlightController: FlashlightController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize VibrationHelper
        vibrationHelper = VibrationHelper(this)
        // Initialize TutorialManager
        tutorialManager = TutorialManager(this)

        // Initialize TextToSpeech
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.US
                textToSpeech.setSpeechRate(speechRate)
                textToSpeech.setPitch(pitch)
            } else {
                Log.e("TTS", "Initialization failed")
            }
        }

        // Request camera permission if not granted
        if (!hasCameraPermission()) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), 0
            )
        }

        setContent {
            // Create the CameraController in the composable
            val controller = remember { LifecycleCameraController(this) }

            // Initialize the FlashlightController with the camera controller
            flashlightController = FlashlightController(controller)

            VAA(
                textToSpeech = textToSpeech,
                speechRate = speechRate,
                pitch = pitch,
                onSpeechRateChange = { newRate ->
                    speechRate = newRate
                    textToSpeech.setSpeechRate(newRate)
                },
                onPitchChange = { newPitch ->
                    pitch = newPitch
                    textToSpeech.setPitch(newPitch)
                },
                vibrationHelper = vibrationHelper,
                tutorialManager = tutorialManager,
                flashlightController = flashlightController,
                controller = controller // Pass the controller to VAA composable
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown() // Cleanup TTS
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

@Composable
fun VAA(
    textToSpeech: TextToSpeech,
    speechRate: Float,
    pitch: Float,
    onSpeechRateChange: (Float) -> Unit,
    onPitchChange: (Float) -> Unit,
    vibrationHelper: VibrationHelper,
    tutorialManager: TutorialManager,
    flashlightController: FlashlightController,
    controller: LifecycleCameraController
) {
    val context = LocalContext.current
    var detections by remember { mutableStateOf(emptyList<Detection>()) }
    var luminance by remember { mutableStateOf(1f) }

    val classifier = remember { TfLiteClassifier(context = context) }
    val analyzer = remember {
        ImageAnalyzer(
            classifier = classifier,
            onResults = { detections = it },
            onLuminanceCalculated = { calculatedLuminance ->
                luminance = calculatedLuminance
            }
        )
    }

    // Set up the camera controller
    LaunchedEffect(Unit) {
        controller.setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
        controller.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            analyzer
        )
    }

    // Automatically turn on the flashlight if luminance is too low
    LaunchedEffect(luminance) {
        Log.d("FlashlightController", "Luminance: $luminance")
        if (luminance < 0.05f && !flashlightController.isFlashlightOn()) {
            flashlightController.turnOnFlashlight()
        } else if (luminance >= 0.1f && flashlightController.isFlashlightOn()) {
            flashlightController.turnOffFlashlight()
        }
    }

    // Track the previously announced objects
    var lastDetectedObject by remember { mutableStateOf("") }

    // Show tutorial dialog for first-time users
    var showTutorial by remember { mutableStateOf(tutorialManager.isFirstTimeUser()) }

    if (showTutorial) {
        TutorialDialog(onDismiss = {
            showTutorial = false
            tutorialManager.markTutorialComplete()
        })
    }

    // Announce detected objects via TTS and trigger vibration
    LaunchedEffect(detections) {
        detections.firstOrNull()?.let { detection ->
            if (detection.label != lastDetectedObject) {
                textToSpeech.speak(detection.label, TextToSpeech.QUEUE_FLUSH, null, null)
                lastDetectedObject = detection.label
                vibrationHelper.vibratePhone() // Vibrate when a new object is detected
            }
        }
    }

    // Main Layout
    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(controller, Modifier.fillMaxSize())

        // Display the labels and scores at the top of the screen
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            detections.forEach { detection ->
                Text(
                    text = "${detection.label} - ${detection.score}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Add Voice Settings UI (Sliders for Speech Rate and Pitch) at the bottom
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f)) // Pushes content to the bottom

            VoiceSettingsScreen(
                speechRate = speechRate,
                pitch = pitch,
                onSpeechRateChange = onSpeechRateChange,
                onPitchChange = onPitchChange
            )
        }
    }
}

@Composable
fun TutorialDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Welcome to VAA!") },
        text = { Text("This is a brief tutorial on how to use the app. You can change the voice settings below and use the camera to detect objects.") },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("Got it!")
            }
        }
    )
}
