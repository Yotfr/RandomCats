package com.yotfr.randomcats.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
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

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                state.isLoading
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
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
