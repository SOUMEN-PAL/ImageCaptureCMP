// androidMain
package com.example.cameraincmp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PhotoLauncherManager(
    private val activity: Activity,
    private val registry: ActivityResultRegistry
) {
    fun launchCamera(continuation: Continuation<String?>) {
        val photoFile = File(
            activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "${SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(Date())}.jpg"
        )

        val uri = FileProvider.getUriForFile(
            activity,
            "${activity.packageName}.provider",
            photoFile
        )

        val launcher = registry.register(
            "takePhotoKey", // unique key
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                continuation.resume(photoFile.absolutePath)
            } else {
                continuation.resume(null)
            }
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        launcher.launch(intent)
    }
}

