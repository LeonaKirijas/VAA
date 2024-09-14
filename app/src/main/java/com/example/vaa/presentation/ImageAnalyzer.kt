package com.example.vaa.presentation

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.vaa.domain.ObjectClassifier
import com.example.vaa.domain.Detection
import android.util.Log

class ImageAnalyzer(
    private val classifier: ObjectClassifier, // Will handle the classification
    private val onResults: (List<Detection>) -> Unit, // Callback to return results
    private val onLuminanceCalculated: (Float) -> Unit // Callback for luminance
) : ImageAnalysis.Analyzer {

    private var frameSkipCounter = 0

    override fun analyze(image: ImageProxy) {
        val luminance = calculateLuminance(image)
        onLuminanceCalculated(luminance) // Pass luminance back to UI
        // Skip frames to reduce load
        if (frameSkipCounter % 60 == 0) {
            val rotationDegrees = image.imageInfo.rotationDegrees
            val bitmap = image.toBitmap() // Convert ImageProxy to Bitmap (you may need to implement this)

            // Perform object detection/classification
            val results = classifier.classify(bitmap, rotationDegrees)

            // Return the results via callback
            onResults(results)
        }
        frameSkipCounter++
        image.close()
    }

    private fun calculateLuminance(image: ImageProxy): Float {
        val buffer = image.planes[0].buffer
        val data = ByteArray(buffer.remaining())
        buffer.get(data)

        var totalLuminance = 0f
        for (pixel in data) {
            totalLuminance += pixel.toInt() and 0xFF
        }
        val luminance = totalLuminance / data.size / 255f
        Log.d("ImageAnalyzer", "Calculated luminance: $luminance")
        return luminance
    }
}
