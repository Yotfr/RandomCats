package com.yotfr.randomcats.domain.usecase.users

import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.model.User
import com.yotfr.randomcats.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetUserProfileInfoUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<Response<User, String>> =
        withContext(Dispatchers.IO) {
            userRepository.getUserProfileInfo()
        }
}
