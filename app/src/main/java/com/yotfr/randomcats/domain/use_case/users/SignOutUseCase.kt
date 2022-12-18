package com.yotfr.randomcats.domain.use_case.users

import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SignOutUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<Response<Unit, String>> =
        withContext(Dispatchers.IO) {
            userRepository.signOut()
        }
}
