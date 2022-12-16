package com.yotfr.randomcats.presentation.screens.resetpassword.model

data class ResetPasswordState(
    val isLoading: Boolean = false,
    val emailText: String = "",
    val isEmailEmptyError: Boolean = false,
    val isEmailInvalidError: Boolean = false,
    val isNoUserWithThisEmailError: Boolean = false
)
