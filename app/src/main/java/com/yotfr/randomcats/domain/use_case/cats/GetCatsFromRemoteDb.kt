package com.yotfr.randomcats.domain.use_case.cats

import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.repository.CatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetCatsFromRemoteDb(
    private val catsRepository: CatsRepository
) {
    suspend operator fun invoke(): Flow<Response<List<Cat>, String>> =
        withContext(Dispatchers.IO) {
            catsRepository.getFromRemoteDb()
        }
}
