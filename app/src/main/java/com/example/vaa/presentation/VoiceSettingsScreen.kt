package com.example.vaa.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VoiceSettingsScreen(
    speechRate: Float,
    pitch: Float,
    onSpeechRateChange: (Float) -> Unit,
    onPitchChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom // Align sliders to the bottom
    ) {
        // Speech Rate Slider
        Text(text = "Speech Rate: ${"%.2f".format(speechRate)}")
        Slider(
            value = speechRate,
            onValueChange = onSpeechRateChange,
            valueRange = 0.5f..2.0f, // Normal range is 0.5x to 2.0x speed
            steps = 4 // Number of stops on the slider
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pitch Slider
        Text(text = "Pitch: ${"%.2f".format(pitch)}")
        Slider(
            value = pitch,
            onValueChange = onPitchChange,
            valueRange = 0.5f..2.0f, // Normal range is 0.5x to 2.0x pitch
            steps = 4
        )
    }
}
