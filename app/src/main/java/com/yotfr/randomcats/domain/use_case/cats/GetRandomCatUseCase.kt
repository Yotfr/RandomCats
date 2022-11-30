package com.yotfr.randomcats.domain.use_case.cats

import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.domain.model.MResult
import com.yotfr.randomcats.domain.repository.CatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetRandomCatUseCase(
    private val catsRepository: CatsRepository
) {

    suspend operator fun invoke(): Flow<MResult<Cat>> = withContext(Dispatchers.IO) {
        catsRepository.getFromApi()
    }
}