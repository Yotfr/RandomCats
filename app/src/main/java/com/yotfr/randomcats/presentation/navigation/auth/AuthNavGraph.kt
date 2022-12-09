package com.yotfr.randomcats.presentation.navigation.auth

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.yotfr.randomcats.presentation.navigation.root.RootGraph
import com.yotfr.randomcats.presentation.screens.signin.SignInScreen
import com.yotfr.randomcats.presentation.screens.signup.SignUpScreen
import soup.compose.material.motion.animation.materialSharedAxisXIn
import soup.compose.material.motion.animation.materialSharedAxisXOut

import soup.compose.material.motion.navigation.composable
import soup.compose.material.motion.navigation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = RootGraph.AUTH,
        startDestination = AuthScreenRoutes.SignIn.route
    ) {
        composable(
            route = AuthScreenRoutes.SignIn.route,
            exitTransition = {
                materialSharedAxisXOut(true, 1000, 300)
            },
            popEnterTransition = {
                materialSharedAxisXIn(false, 1000, 300)
            }
        ) {
            SignInScreen(
                toSignUp = {
                    navController.navigate(AuthScreenRoutes.SignUp.route)
                },
                toMain = {
                    navController.navigate(RootGraph.HOME) {
                        popUpTo(RootGraph.ROOT)
                    }
                }
            )
        }
        composable(
            route = AuthScreenRoutes.SignUp.route,
            enterTransition = {
                materialSharedAxisXIn(true, 1000, 300)
            },
            popExitTransition = {
                materialSharedAxisXOut(false, 1000, 300)
            }
        ) {
            SignUpScreen(
                backToSignIn = {
                    navController.popBackStack()
                }
            )
        }
    }
}


