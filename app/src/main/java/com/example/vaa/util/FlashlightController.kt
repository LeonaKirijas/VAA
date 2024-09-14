package com.example.vaa.util

import androidx.camera.view.CameraController

class FlashlightController(private val cameraController: CameraController) {

    private var isFlashlightOn: Boolean = false

    fun toggleFlashlight() {
        isFlashlightOn = !isFlashlightOn
        cameraController.enableTorch(isFlashlightOn)
    }

    fun turnOnFlashlight() {
        if (!isFlashlightOn) {
            isFlashlightOn = true
            cameraController.enableTorch(true)
        }
    }

    fun turnOffFlashlight() {
        if (isFlashlightOn) {
            isFlashlightOn = false
            cameraController.enableTorch(false)
        }
    }

    fun isFlashlightOn() = isFlashlightOn
}
