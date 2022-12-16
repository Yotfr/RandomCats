package com.yotfr.randomcats.domain.use_case.users

data class UserUseCases(
    val signUpUserUseCase: SignUpUserUseCase,
    val signInUserUseCase: SignInUserUseCase,
    val checkUserSignUseCase: CheckUserSignUseCase,
    val signOutUseCase: SignOutUseCase,
    val resetPasswordUseCase: ResetPasswordUseCase
)