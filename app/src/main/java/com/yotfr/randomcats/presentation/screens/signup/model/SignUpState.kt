package com.yotfr.randomcats.presentation.screens.signup.model

data class SignUpState(
    val isLoading:Boolean = false,
    val isSuccess: Boolean = false,
    val emailText:String = "",
    val passwordText:String = "",
    val confirmPasswordText:String = "",
    val isEmailEmptyError:Boolean = false,
    val isPasswordEmptyError:Boolean = false,
    val isEmailInvalidError:Boolean = false,
    val isUserAlreadyExistsException:Boolean = false,
    val isPasswordsNotMatchError:Boolean = false,
    val isPasswordTooShortError:Boolean = false,
)
