package com.yotfr.randomcats.domain.use_case.users

import com.yotfr.randomcats.domain.model.MResult
import com.yotfr.randomcats.domain.model.SignInModel
import com.yotfr.randomcats.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SignInUserUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(signInModel: SignInModel): Flow<MResult<Unit>> =
        withContext(Dispatchers.IO) {
            userRepository.signInUser(signInModel)
        }

}