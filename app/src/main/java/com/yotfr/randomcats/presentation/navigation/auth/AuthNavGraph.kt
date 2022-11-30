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
        startDestination = AuthScreen.SignIn.route
    ) {
        composable(route = AuthScreen.SignIn.route){
            SignInScreen(
                onSignUp = {
                    navController.navigate(AuthScreen.SignUp.route)
                },
                onSignIn = {
                    navController.popBackStack()
                    navController.navigate(RootGraph.HOME)
                }
            )
        }
        composable(route = AuthScreen.SignUp.route){
            SignUpScreen()
        }
    }
}

