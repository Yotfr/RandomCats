package com.yotfr.randomcats.presentation.navigation.root

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yotfr.randomcats.presentation.navigation.auth.authNavGraph
import com.yotfr.randomcats.presentation.navigation.home.HomeScreen
import com.yotfr.randomcats.presentation.navigation.splash.splashNavigationGraph

@Composable
fun RootNavigationGraph (
    navController: NavHostController
){
    NavHost (
        navController = navController,
        route = RootGraph.ROOT,
        startDestination = RootGraph.SPLASH
    ){
        splashNavigationGraph(navController = navController)
        authNavGraph(navController = navController)
        composable(route = RootGraph.HOME){
            HomeScreen(rootNavController = navController)
        }
    }
}