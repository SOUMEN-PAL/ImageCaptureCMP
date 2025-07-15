package com.example.cameraincmp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable


@Serializable
data object HomeRoute

@Serializable
data object ImageCaptureRoute

@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ){

        composable<HomeRoute> {
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