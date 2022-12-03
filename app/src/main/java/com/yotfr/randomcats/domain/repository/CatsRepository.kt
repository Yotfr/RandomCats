package com.yotfr.randomcats.domain.repository

import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface CatsRepository {

    suspend fun getFromApi(): Flow<Response<Cat,String>>

    suspend fun uploadToRemoteDb(cat:Cat)

    suspend fun getFromRemoteDb():Flow<Response<List<Cat>,String>>

    suspend fun deleteFromRemoteDb(cat:Cat)

}