package com.example.cameraincmp

import org.koin.mp.KoinPlatform.getKoin
import kotlin.coroutines.suspendCoroutine

actual suspend fun TakePhoto(): String? = suspendCoroutine { continuation ->
    val manager = getKoin().get<PhotoLauncherManager>()
    manager.launchCamera(continuation)
}