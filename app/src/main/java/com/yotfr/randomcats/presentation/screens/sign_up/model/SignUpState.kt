package com.yotfr.randomcats.presentation.screens.sign_up.model

data class SignUpState(
    val isLoading:Boolean = false,
    val isSuccess: Boolean = false,
    val error:String = ""
)
