package com.yotfr.randomcats.domain.repository

import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface CatsRepository {
    // get random cat
    suspend fun getFromApi(): Flow<Response<Cat, String>>

    // upload cat to fireStore
    suspend fun uploadToRemoteDb(cat: Cat): Flow<Response<Unit, String>>

    // get all cats from fireStore
    suspend fun getFromRemoteDb(): Flow<Response<List<Cat>, String>>

    // delete cat from fireStore
    suspend fun deleteFromRemoteDb(cat: Cat): Flow<Response<Unit, String>>
}
