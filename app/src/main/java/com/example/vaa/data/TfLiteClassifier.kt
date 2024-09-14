package com.example.vaa.data

import android.content.Context
import android.graphics.Bitmap
import android.view.Surface
import com.example.vaa.domain.ObjectClassifier
import com.example.vaa.domain.Detection
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.detector.ObjectDetector

class TfLiteClassifier(
    private val context: Context,
    private val threshold: Float = 0.5f,
    private val maxResults: Int = 3
) : ObjectClassifier {

    private var detector: ObjectDetector? = null

    // Initialize the model and options
    private fun setupClassifier() {
        val baseOptions = BaseOptions.builder()
            .setNumThreads(2)
            .build()
        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(maxResults)
            .setScoreThreshold(threshold)
            .build()

        try {
            detector = ObjectDetector.createFromFileAndOptions(
                context,
                "efficientdet-lite1.tflite", // EfficientDet-Lite1 model
                options
            )
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    // Classify method processes the bitmap and returns detections
    override fun classify(bitmap: Bitmap, rotation: Int): List<Detection> {
        if (detector == null) {
            setupClassifier()
        }

        val imageProcessor = ImageProcessor.Builder().build()
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

        val imageProcessingOptions = ImageProcessingOptions.builder()
            .setOrientation(getOrientationFromRotation(rotation))
            .build()

        val results = detector?.detect(tensorImage, imageProcessingOptions)

        // Parse results and return detection objects
        return results?.map { result ->
            Detection(
                label = result.categories.firstOrNull()?.label ?: "Unknown",
                score = result.categories.firstOrNull()?.score ?: 0f,
                boundingBox = result.boundingBox // Bounding box for drawing
            )
        } ?: emptyList()
    }

    private fun getOrientationFromRotation(rotation: Int): ImageProcessingOptions.Orientation {
        return when (rotation) {
            Surface.ROTATION_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            else -> ImageProcessingOptions.Orientation.RIGHT_TOP
        }
    }
}
