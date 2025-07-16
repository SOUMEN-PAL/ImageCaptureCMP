package com.example.cameraincmp

actual suspend fun TakePhoto(): String? {
    return takePhotoIOS()
}