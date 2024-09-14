package com.example.vaa.domain

import android.graphics.RectF

// Data class to hold object detection results
data class Detection(
    val label: String,
    val score: Float,
    val boundingBox: RectF // The bounding box around the detected object
)
