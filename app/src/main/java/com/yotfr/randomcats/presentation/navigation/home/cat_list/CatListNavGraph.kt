package com.yotfr.randomcats.presentation.navigation.home.cat_list

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.yotfr.randomcats.presentation.navigation.home.BottomNavScreens
import com.yotfr.randomcats.presentation.navigation.root.RootGraph
import com.yotfr.randomcats.presentation.screens.cats_list_screen.GridListScreen
import com.yotfr.randomcats.presentation.screens.cats_list_screen.HorizontalPagerScreen
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut
import soup.compose.material.motion.navigation.composable
import soup.compose.material.motion.navigation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.catListNavGraph(navController: NavHostController) {

    val onBackPressed: (args: String?) -> Unit = {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set("currentIndex", it)
        navController.popBackStack()
    }

    navigation(
        route = RootGraph.FAVORITE,
        startDestination = BottomNavScreens.Favorite.screen_route
    ) {
        composable(
            route = BottomNavScreens.Favorite.screen_route,
            enterTransition = {
                materialFadeThroughIn()
            },
            popExitTransition = {
                materialFadeThroughOut()
            }
        ) {
            GridListScreen(
                navController = navController,
                currentIndex = navController.currentBackStackEntry?.savedStateHandle?.get(
                    "currentIndex"
                )
            )
        }
        composable("pager/{indexFromGrid}") { backStackEntry ->
            HorizontalPagerScreen(
                onBackPressed = { currentIndex ->
                    onBackPressed(
                        currentIndex
                    )
                },
                pageFromGrid = backStackEntry.arguments?.getString("indexFromGrid")
            )
        }
    }
}