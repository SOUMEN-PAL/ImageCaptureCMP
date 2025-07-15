package com.example.cameraincmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform