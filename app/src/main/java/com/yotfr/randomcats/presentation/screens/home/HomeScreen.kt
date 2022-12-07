package com.yotfr.randomcats.presentation.screens.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yotfr.randomcats.presentation.navigation.home.BottomNavScreens
import com.yotfr.randomcats.presentation.navigation.home.HomeNavGraph
import soup.compose.material.motion.navigation.rememberMaterialMotionNavController

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val navController = rememberMaterialMotionNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController)}
    ) {
        Box(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
            HomeNavGraph(navController = navController)
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {

    val screens = listOf(
        BottomNavScreens.Home,
        BottomNavScreens.Favorite,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.screen_route == currentDestination?.route }
    if (bottomBarDestination) {
        NavigationBar {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }

}

@Composable
fun RowScope.AddItem(
    screen: BottomNavScreens,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.screen_route
        } == true,
        onClick = {
            navController.navigate(screen.screen_route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}