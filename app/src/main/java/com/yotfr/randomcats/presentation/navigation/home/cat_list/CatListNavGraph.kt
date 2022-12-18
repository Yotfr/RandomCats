package com.yotfr.randomcats.presentation.navigation.home.cat_list

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.yotfr.randomcats.presentation.navigation.home.BottomNavScreens
import com.yotfr.randomcats.presentation.navigation.root.RootGraph
import com.yotfr.randomcats.presentation.screens.gridcatlist.GridListScreen
import com.yotfr.randomcats.presentation.screens.pagercatlist.HorizontalPagerScreen
import soup.compose.material.motion.animation.*
import soup.compose.material.motion.navigation.composable
import soup.compose.material.motion.navigation.navigation
import kotlin.math.sign

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.catListNavGraph(
    navController: NavHostController,
    signOut: () -> Unit
) {
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
                fadeOut()
            },
            popEnterTransition = {
                materialElevationScaleIn(
                    initialScale = 1f
                )
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
                )?.toInt() ?: 0,
                navigateToAuth = {
                    signOut()
                }
            )
        }
        composable(
            route = CatListScreenRoute.Details.screen_route,
            enterTransition = {
                materialElevationScaleIn(
                    initialScale = 0.5f
                )
            },
            popExitTransition = {
                materialElevationScaleOut(
                    targetScale = 0.5f
                )
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
