package com.example.vaa.domain

import android.graphics.Bitmap

// Interface for object classification
interface ObjectClassifier {
    fun classify(bitmap: Bitmap, rotation: Int): List<Detection>
}
