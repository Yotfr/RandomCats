package com.yotfr.randomcats.domain.use_case.users

import com.yotfr.randomcats.domain.repository.UserRepository


class CheckUserSignUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Boolean = userRepository.isSignedIn()
}