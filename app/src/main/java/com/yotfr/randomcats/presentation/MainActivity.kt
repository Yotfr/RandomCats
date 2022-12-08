package com.yotfr.randomcats.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yotfr.randomcats.domain.model.Theme
import com.yotfr.randomcats.presentation.navigation.root.RootNavigationGraph
import com.yotfr.randomcats.presentation.theme.RandomCatsTheme
import dagger.hilt.android.AndroidEntryPoint
import soup.compose.material.motion.navigation.rememberMaterialMotionNavController

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val state by viewModel.state
        Log.d("TEST","${state.theme}")

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                state.isLoading
            }
        }

        setContent {
            RandomCatsTheme(
                theme = state.theme
            ) {
                RootNavigationGraph(
                    navController = rememberMaterialMotionNavController(),
                    startDestination = state.startDestinationRoute
                )
            }
        }

    }
}







