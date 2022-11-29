package com.yotfr.randomcats.domain.repository

import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.domain.model.MResult
import kotlinx.coroutines.flow.Flow

interface CatsRepository {

    suspend fun getFromApi(): Flow<MResult<Cat>>

    suspend fun uploadToRemoteDb(cat:Cat)

    suspend fun getFromRemoteDb():Flow<MResult<List<Cat>>>

}