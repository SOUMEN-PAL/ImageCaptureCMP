package com.example.cameraincmp

import androidx.compose.runtime.Composable

@Composable
actual fun PlatformCameraCaptureScreen(onFinish: () -> Unit) {
    CameraScreen(onFinish = onFinish)
}