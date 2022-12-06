package com.yotfr.randomcats.presentation.navigation.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yotfr.randomcats.presentation.navigation.root.RootGraph
import com.yotfr.randomcats.presentation.screens.cats_list_screen.GridListScreen
import com.yotfr.randomcats.presentation.screens.cats_list_screen.HorizontalPagerScreen
import com.yotfr.randomcats.presentation.screens.profile.ProfileScreen
import com.yotfr.randomcats.presentation.screens.random_cat_screen.PickerScreen
import soup.compose.material.motion.navigation.MaterialMotionNavHost
import soup.compose.material.motion.navigation.composable
import soup.compose.material.motion.navigation.rememberMaterialMotionNavController


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(rootNavController:NavHostController) {

    val navController = rememberMaterialMotionNavController()

    val navItems = listOf(
        BottomNavItem.Favorite,
        BottomNavItem.Home,
        BottomNavItem.Profile
    )

    val onBackPressed: (args: String?) -> Unit = {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set("currentIndex", it)
        navController.popBackStack()
    }

    val bottomNavBarVisible = navController.currentBackStackEntryAsState()
        .value?.destination?.route in navItems.map { it.screen_route }

    Scaffold(
        bottomBar = {
            if (bottomNavBarVisible) {
                NavigationBar {

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    navItems.forEachIndexed { _, bottomNavItem ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    painter = painterResource(id = bottomNavItem.icon),
                                    contentDescription = bottomNavItem.title
                                )
                            },
                            label = { Text(text = bottomNavItem.title) },
                            selected = currentRoute == bottomNavItem.screen_route,
                            onClick = {
                                navController.navigate(bottomNavItem.screen_route) {
                                    navController.graph.startDestinationRoute?.let { screenRoute ->
                                        popUpTo(screenRoute) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MaterialMotionNavHost(
                route = RootGraph.HOME,
                navController = navController,
                startDestination = BottomNavItem.Home.screen_route
            ) {

                composable(BottomNavItem.Favorite.screen_route) {
                    GridListScreen(
                        navController = navController,
                        currentIndex = navController.currentBackStackEntry?.savedStateHandle?.get(
                            "currentIndex"
                        )
                    )
                }
                composable(BottomNavItem.Home.screen_route) {
                    PickerScreen()
                }
                composable(BottomNavItem.Profile.screen_route) {
                    ProfileScreen(
                        navigateToAuth = {
                            rootNavController.popBackStack()
                            rootNavController.navigate(RootGraph.AUTH)
                        }
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
    }


}