package com.yotfr.randomcats.presentation.navigation.splash

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yotfr.randomcats.presentation.navigation.root.RootGraph
import com.yotfr.randomcats.presentation.screens.splash.SplashScreen

fun NavGraphBuilder.splashNavigationGraph(navController: NavHostController){
    navigation(
        route = RootGraph.SPLASH,
        startDestination = "splash"
    ) {
        composable(route = "splash"){
            SplashScreen(
                onHome = {
                    navController.popBackStack()
                    navController.navigate(RootGraph.HOME)
                },
                onAuth = {
                    navController.popBackStack()
                    navController.navigate(RootGraph.AUTH)
                }
            )
        }
    }
}