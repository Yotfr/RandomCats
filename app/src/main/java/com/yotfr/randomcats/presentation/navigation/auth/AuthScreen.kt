package com.yotfr.randomcats.presentation.navigation.auth

sealed class AuthScreen(val route:String) {
    object SignIn: AuthScreen(route = "SIGN_IN")
    object SignUp: AuthScreen(route = "SIGN_UP")
}
