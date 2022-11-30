package com.yotfr.randomcats.presentation.screens.sign_in.model

data class SignInState(
    val isLoading:Boolean = false,
    val isSuccess: Boolean = false,
    val error:String = ""
)
