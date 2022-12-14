package com.yotfr.randomcats.presentation.screens.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.navigation.home.BottomNavScreens
import com.yotfr.randomcats.presentation.navigation.home.HomeNavGraph
import com.yotfr.randomcats.presentation.navigation.settings.SettingsScreenRoute
import soup.compose.material.motion.navigation.rememberMaterialMotionNavController

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    signOut: () -> Unit
) {
    val navController = rememberMaterialMotionNavController()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val screens = listOf(
        BottomNavScreens.Home,
        BottomNavScreens.Favorite
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val systemBarsVisible = screens.any { it.screen_route == currentDestination?.route }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = { BottomBar(navController = navController) },
        topBar = {
            TopBar(
                navController = navController,
                onSettingsClicked = {
                    navController.navigate(SettingsScreenRoute.SettingsRoute.screen_route)
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(
                    bottom = if (systemBarsVisible) it.calculateBottomPadding()
                    else 0.dp,
                    top = if (systemBarsVisible) it.calculateTopPadding()
                    else 0.dp
                )
        ) {
            HomeNavGraph(
                navController = navController,
                signOut = { signOut() }
            )
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController
) {
    val screens = listOf(
        BottomNavScreens.Home,
        BottomNavScreens.Favorite
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.screen_route == currentDestination?.route }
    if (bottomBarDestination) {
        NavigationBar(
            tonalElevation = 1.dp,
            windowInsets = WindowInsets.systemBars
                .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
        ) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController,
    onSettingsClicked: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val screens = listOf(
        BottomNavScreens.Home,
        BottomNavScreens.Favorite
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.screen_route == currentDestination?.route }

    if (bottomBarDestination) {
        CenterAlignedTopAppBar(
            title = {
                when (currentDestination?.route) {
                    BottomNavScreens.Home.screen_route -> {
                        Text(text = BottomNavScreens.Home.title)
                    }
                    BottomNavScreens.Favorite.screen_route -> {
                        Text(text = BottomNavScreens.Favorite.title)
                    }
                }
            },
            navigationIcon = {},
            actions = {
                IconButton(onClick = { onSettingsClicked() }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(id = R.string.settings),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            scrollBehavior = scrollBehavior,
            windowInsets = WindowInsets.systemBars
                .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
        )
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavScreens,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                elevation = 1.dp
            ),
            unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.5f
            ),
            unselectedTextColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.5f
            )
        ),
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
