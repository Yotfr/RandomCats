package com.yotfr.randomcats.domain.use_case.cats

import android.util.Log
import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.repository.CatsRepository
import com.yotfr.randomcats.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UploadCatToRemoteDbUseCase(
    private val catsRepository: CatsRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(cat: Cat): Flow<Response<Unit, String>> =
        withContext(Dispatchers.IO) {
            val userId = userRepository.getCurrentUserUid()
            Log.d("UPTEST","userId -> $userId")
            catsRepository.uploadToRemoteDb(cat, userId)
        }
}
