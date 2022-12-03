package com.yotfr.randomcats.presentation.navigation.auth

sealed class AuthScreenRoutes(val route:String) {
    object SignIn: AuthScreenRoutes(route = "SIGN_IN")
    object SignUp: AuthScreenRoutes(route = "SIGN_UP")
}
