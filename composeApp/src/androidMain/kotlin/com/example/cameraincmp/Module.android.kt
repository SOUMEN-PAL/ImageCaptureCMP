package com.example.cameraincmp

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {
    single<Context> { androidContext() }

    single {
        PhotoLauncherManager(
            activity = get<Activity>(),
            registry = get<ActivityResultRegistry>()
        )
    }
}