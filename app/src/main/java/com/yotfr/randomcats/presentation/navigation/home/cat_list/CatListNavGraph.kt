package com.yotfr.randomcats.presentation.navigation.home.cat_list

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.yotfr.randomcats.presentation.navigation.home.BottomNavScreens
import com.yotfr.randomcats.presentation.navigation.root.RootGraph
import com.yotfr.randomcats.presentation.screens.grid_cat_list.GridListScreen
import com.yotfr.randomcats.presentation.screens.pager_cat_list.HorizontalPagerScreen
import soup.compose.material.motion.animation.materialElevationScaleIn
import soup.compose.material.motion.animation.materialElevationScaleOut
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut
import soup.compose.material.motion.navigation.composable
import soup.compose.material.motion.navigation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.catListNavGraph(navController: NavHostController) {

    val onBackPressed: (args: String?) -> Unit = {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set(DETAILS_SELECTED_INDEX_KEY, it)
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
            },
            exitTransition = {
                materialElevationScaleOut()
            },
            popEnterTransition = {
                materialElevationScaleIn()
            }
        ) {
            GridListScreen(
                navigateToImageDetails = { selectedIndex ->
                    navController.navigate(
                        route = CatListScreenRoute.Details.passSelectedIndex(
                            selectedIndex = selectedIndex
                        )
                    )
                },
                selectedIndex = navController.currentBackStackEntry?.savedStateHandle?.get<String>(
                    DETAILS_SELECTED_INDEX_KEY
                )?.toInt() ?: 0
            )
        }
        composable(
            route = CatListScreenRoute.Details.screen_route,
            enterTransition = {
                materialElevationScaleIn()
            },
            popExitTransition = {
                materialElevationScaleOut()
            }
        ) { backStackEntry ->
            HorizontalPagerScreen(
                onBackPressed = { selectedIndex ->
                    onBackPressed(
                        selectedIndex.toString()
                    )
                },
                selectedIndex =
                backStackEntry.arguments?.getString(DETAILS_SELECTED_INDEX_KEY)?.toInt() ?: 0
            )

        }
    }
}