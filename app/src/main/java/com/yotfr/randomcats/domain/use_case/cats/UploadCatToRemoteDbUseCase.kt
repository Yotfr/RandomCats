package com.yotfr.randomcats.domain.use_case.cats

import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.repository.CatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UploadCatToRemoteDbUseCase(
    private val catsRepository: CatsRepository
) {
    suspend operator fun invoke(cat: Cat): Flow<Response<Unit, String>> =
        withContext(Dispatchers.IO) {
            catsRepository.uploadToRemoteDb(cat)
        }
}
