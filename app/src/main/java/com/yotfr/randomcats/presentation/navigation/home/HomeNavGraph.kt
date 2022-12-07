package com.yotfr.randomcats.presentation.navigation.home

import androidx.compose.animation.ExperimentalAnimationApi

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import com.yotfr.randomcats.presentation.navigation.home.cat_list.catListNavGraph
import com.yotfr.randomcats.presentation.navigation.root.RootGraph
import com.yotfr.randomcats.presentation.screens.random_cat_screen.RandomCatScreen
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut
import soup.compose.material.motion.navigation.MaterialMotionNavHost
import soup.compose.material.motion.navigation.composable


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeNavGraph(navController:NavHostController) {

    MaterialMotionNavHost(
        route = RootGraph.HOME,
        navController = navController,
        startDestination = BottomNavScreens.Home.screen_route
    ) {
        composable(
            route = BottomNavScreens.Home.screen_route,
            popEnterTransition = {
                materialFadeThroughIn()
            },
            exitTransition = {
                materialFadeThroughOut()
            }
        ) {
            RandomCatScreen()
        }

        catListNavGraph(navController = navController)

    }
}