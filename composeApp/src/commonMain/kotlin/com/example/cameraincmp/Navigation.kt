package com.example.cameraincmp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.camera.CAMERA
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@Serializable
data object HomeRoute

@Serializable
data object ImageCaptureRoute

@Composable
fun Navigation(){
    val navController = rememberNavController()

    var locationPermissionState by mutableStateOf(PermissionState.NotDetermined)
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) {
        factory.createPermissionsController()
    }
    BindEffect(controller)

    suspend fun provideOrRequestLocationPermissionWithCallback(onResult: () -> Unit) {
        try {
            controller.providePermission(Permission.CAMERA)
            locationPermissionState = PermissionState.Granted
        } catch (e: DeniedAlwaysException) {
            locationPermissionState = PermissionState.DeniedAlways
        } catch (e: DeniedException) {
            locationPermissionState = PermissionState.Denied
        } catch (e: RequestCanceledException) {
            e.printStackTrace()
        } finally {
            onResult()
        }
    }



    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ){

        composable<HomeRoute> {

            LaunchedEffect(
             Unit
            ) {
                provideOrRequestLocationPermissionWithCallback(
                    onResult = {

                    }
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        navController.navigate(ImageCaptureRoute)
                    }
                ) {
                    val data = CameraResultBridge.photoPathFlow.collectAsState()
                    Text("Go to Camera ${data.value}")
                }
            }
        }


        composable<ImageCaptureRoute> {

            PlatformCameraCaptureScreen {
                navController.navigate(HomeRoute)
            }

        }

    }

}