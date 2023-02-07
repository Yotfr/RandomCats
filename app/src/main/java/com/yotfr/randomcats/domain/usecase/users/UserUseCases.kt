package com.yotfr.randomcats.domain.usecase.users

data class UserUseCases(
    val signUpUserUseCase: SignUpUserUseCase,
    val signInUserUseCase: SignInUserUseCase,
    val checkUserSignUseCase: CheckUserSignUseCase,
    val signOutUseCase: SignOutUseCase,
    val resetPasswordUseCase: ResetPasswordUseCase,
    val getUserProfileInfoUseCase: GetUserProfileInfoUseCase
)
