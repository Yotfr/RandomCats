package com.yotfr.randomcats.presentation.screens.signin.model

data class SignInState(
    val isLoading:Boolean = false,
    val isSuccess: Boolean = false,
    val emailText:String = "",
    val passwordText:String = "",
    val isEmailEmptyError:Boolean = false,
    val isPasswordEmptyError:Boolean = false,
    val isEmailInvalidError:Boolean = false,
    val isInvalidCredentialsError:Boolean = false
)
