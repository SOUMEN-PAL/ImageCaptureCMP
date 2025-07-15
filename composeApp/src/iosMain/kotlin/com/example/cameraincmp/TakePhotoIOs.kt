package com.example.cameraincmp

import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUUID
import platform.Foundation.NSUserDomainMask
import platform.Foundation.writeToFile
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject
import kotlin.coroutines.suspendCoroutine

suspend fun takePhotoIOS(): String? = suspendCoroutine { continuation ->
    val picker = UIImagePickerController().apply {
        sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
        allowsEditing = false
    }

    class Delegate : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {
        override fun imagePickerController(
            picker: UIImagePickerController,
            didFinishPickingMediaWithInfo: Map<Any?, *>
        ) {
            val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
            val data = image?.let { UIImageJPEGRepresentation(it, 1.0) }
            val documents = NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory,
                NSUserDomainMask,
                true
            ).firstOrNull() as? String

            val filename = "${NSUUID().UUIDString}.jpg"
            val path = documents?.let { "$it/$filename" }

            if (path != null && data != null) {
                data.writeToFile(path, true)
                CameraResultBridge.photoPathFlow.value = path
            } else {
                CameraResultBridge.photoPathFlow.value = null
            }

            picker.dismissViewControllerAnimated(true, completion = null)
        }

        override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
            picker.dismissViewControllerAnimated(true, completion = null)
            CameraResultBridge.photoPathFlow.value = null
        }
    }

    val delegate = Delegate()
    picker.delegate = delegate

    // Present the picker using root view controller
    val window = platform.UIKit.UIApplication.sharedApplication.keyWindow
    val rootVC = window?.rootViewController
    rootVC?.presentViewController(picker, true, completion = null)
}
