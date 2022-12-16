package com.yotfr.randomcats.domain.use_case.users

import com.yotfr.randomcats.domain.model.ResetPasswordModel
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ResetPasswordUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(resetPasswordModel: ResetPasswordModel): Flow<Response<Unit, String>> =
        withContext(Dispatchers.IO) {
            userRepository.sendResetPasswordLink(
                resetPasswordModel = resetPasswordModel
            )
        }
}