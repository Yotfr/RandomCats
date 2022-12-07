package com.yotfr.randomcats.presentation.navigation.root

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.yotfr.randomcats.presentation.navigation.auth.authNavGraph
import com.yotfr.randomcats.presentation.screens.home.HomeScreen
import soup.compose.material.motion.navigation.MaterialMotionNavHost
import soup.compose.material.motion.navigation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootNavigationGraph (
    navController: NavHostController,
    startDestination:String
){
    MaterialMotionNavHost (
        navController = navController,
        route = RootGraph.ROOT,
        startDestination = startDestination
    ){
        authNavGraph(navController = navController)
        composable(route = RootGraph.HOME){
            HomeScreen()
        }
    }
}