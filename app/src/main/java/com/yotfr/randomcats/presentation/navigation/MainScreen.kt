package com.yotfr.randomcats.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yotfr.randomcats.presentation.screens.cats_list_screen.GridListScreen
import com.yotfr.randomcats.presentation.screens.cats_list_screen.HorizontalPagerScreen
import com.yotfr.randomcats.presentation.screens.profile.ProfileScreen
import com.yotfr.randomcats.presentation.screens.random_cat_screen.PickerScreen


@Composable
fun MainScreen(){

    val navController = rememberNavController()

    val navItems = listOf(
        BottomNavItem.Favorite,
        BottomNavItem.Home,
        BottomNavItem.Profile
    )

    val onBackPressed: () -> Unit = {
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
                            icon = {Icon(painter = painterResource(id = bottomNavItem.icon), contentDescription = bottomNavItem.title)},
                            label = { Text(text = bottomNavItem.title) },
                            selected = currentRoute == bottomNavItem.screen_route,
                            onClick = {
                                navController.navigate(bottomNavItem.screen_route){
                                    navController.graph.startDestinationRoute?.let { screenRoute ->
                                        popUpTo(screenRoute){
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
            NavHost (
                navController = navController,
                startDestination = BottomNavItem.Home.screen_route
            ) {

                composable(BottomNavItem.Favorite.screen_route){
                    GridListScreen(navController = navController)
                }
                composable(BottomNavItem.Home.screen_route){
                    PickerScreen()
                }
                composable(BottomNavItem.Profile.screen_route){
                    ProfileScreen()
                }


                composable("pager"){
                    HorizontalPagerScreen(onBackPressed = onBackPressed)
                }

            }
        }
    }
    

}