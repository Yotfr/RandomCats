package com.yotfr.randomcats.presentation.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yotfr.randomcats.presentation.navigation.root.RootGraph
import com.yotfr.randomcats.presentation.screens.sign_in.SignInScreen
import com.yotfr.randomcats.presentation.screens.sign_up.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController){
    navigation(
        route = RootGraph.AUTH,
        startDestination = AuthScreenRoutes.SignIn.route
    ) {
        composable(route = AuthScreenRoutes.SignIn.route){
            SignInScreen(
                toSignUp = {
                    navController.navigate(AuthScreenRoutes.SignUp.route)
                },
                toMain = {
                    navController.navigate(RootGraph.HOME){
                        popUpTo(RootGraph.ROOT)
                    }
                }
            )
        }
        composable(route = AuthScreenRoutes.SignUp.route){
            SignUpScreen(
                backToSignIn = {
                    navController.popBackStack()
                }
            )
        }
    }
}

