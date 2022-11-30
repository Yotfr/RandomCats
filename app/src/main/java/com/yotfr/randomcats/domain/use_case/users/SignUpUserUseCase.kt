package com.yotfr.randomcats.domain.use_case.users

import com.yotfr.randomcats.domain.model.MResult
import com.yotfr.randomcats.domain.model.SignUpModel
import com.yotfr.randomcats.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class SignUpUserUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(signUpModel: SignUpModel): Flow<MResult<Unit>> =
        withContext(Dispatchers.IO) {
            userRepository.signUpUser(signUpModel)
        }

}