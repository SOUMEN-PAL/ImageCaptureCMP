package com.example.cameraincmp

import kotlinx.coroutines.flow.MutableStateFlow

object CameraResultBridge {
    val photoPathFlow = MutableStateFlow<String?>(null)
}