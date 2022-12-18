package com.yotfr.randomcats.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.yotfr.randomcats.base.isPermanentlyDenied
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
