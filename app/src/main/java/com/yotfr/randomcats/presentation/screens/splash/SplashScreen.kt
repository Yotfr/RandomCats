package com.yotfr.randomcats.presentation.screens.splash

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.yotfr.randomcats.presentation.screens.splash.event.SplashScreenEvent

@Composable
fun SplashScreen(
    onAuth:() -> Unit,
    onHome:() -> Unit,
    viewModel:SplashViewModel = hiltViewModel()
){

    val event = viewModel.event

    LaunchedEffect(key1 = event) {
        Log.d("TEST","launched")
        viewModel.event.collect{
            Log.d("TEST","$it")
            when(it) {
                SplashScreenEvent.NavigateAuth -> {
                    onAuth()
                }
                SplashScreenEvent.NavigateHome -> {
                    onHome()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(color = Color.Blue)) {

    }
}