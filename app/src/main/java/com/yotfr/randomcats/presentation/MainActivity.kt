package com.yotfr.randomcats.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
        val startDestination = viewModel.startDestination.value

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        setContent {
            RandomCatsTheme {
                ConfigureSystemBars()
                RootNavigationGraph(
                    navController = rememberMaterialMotionNavController(),
                    startDestination = startDestination
                )
            }
        }
    }

    @Composable
    fun ConfigureSystemBars() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = isSystemInDarkTheme()

        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = !useDarkIcons
            )
        }
    }
}





