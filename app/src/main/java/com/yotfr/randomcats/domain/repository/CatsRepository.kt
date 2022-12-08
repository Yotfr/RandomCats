package com.yotfr.randomcats.domain.repository

import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface CatsRepository {

    suspend fun getFromApi(): Flow<Response<Cat,String>>

    suspend fun uploadToRemoteDb(cat:Cat, userId: String):Flow<Response<Unit,String>>

    suspend fun getFromRemoteDb(userId: String):Flow<Response<List<Cat>,String>>

    suspend fun deleteFromRemoteDb(cat:Cat, userId:String)

}