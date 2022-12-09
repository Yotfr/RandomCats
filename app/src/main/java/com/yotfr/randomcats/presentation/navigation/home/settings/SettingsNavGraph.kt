package com.yotfr.randomcats.presentation.navigation.home.settings

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.yotfr.randomcats.presentation.navigation.root.RootGraph
import com.yotfr.randomcats.presentation.screens.settings.SettingsScreen
import com.yotfr.randomcats.presentation.screens.settingstheme.SettingsThemeScreen
import soup.compose.material.motion.navigation.composable
import soup.compose.material.motion.navigation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.settingsNavGraph(navController: NavHostController) {
    navigation(
        route = RootGraph.SETTINGS,
        startDestination = SettingsScreenRoute.SettingsRoute.screen_route
    ) {
        composable(
            route = SettingsScreenRoute.SettingsRoute.screen_route
        ) {
            SettingsScreen(
                navigateToAuth = {
                    navController.navigate(RootGraph.AUTH) {
                        popUpTo(RootGraph.ROOT)
                    }
                },
                navigateToThemeScreen = { navController.navigate(SettingsScreenRoute.SettingsThemeRoute.screen_route) },
                goBack = { navController.popBackStack() }
            )
        }
        composable(
            route = SettingsScreenRoute.SettingsThemeRoute.screen_route
        ) {
            SettingsThemeScreen(
                goBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
