package com.yotfr.randomcats.domain.usecase.users

import com.yotfr.randomcats.domain.repository.UserRepository

class CheckUserSignUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Boolean = userRepository.isSignedIn()
}
