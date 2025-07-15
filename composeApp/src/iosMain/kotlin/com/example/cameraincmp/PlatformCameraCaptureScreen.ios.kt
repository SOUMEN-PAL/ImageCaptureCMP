package com.example.cameraincmp

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
actual fun PlatformCameraCaptureScreen(onFinish: () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        takePhotoIOS()
        onFinish()
    }
}