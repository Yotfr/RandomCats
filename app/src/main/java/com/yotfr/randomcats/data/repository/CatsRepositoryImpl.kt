package com.yotfr.randomcats.data.repository

import android.util.Log
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.yotfr.randomcats.base.ext.snapshotFlow
import com.yotfr.randomcats.data.data_source.CatsApi
import com.yotfr.randomcats.data.dto.CatFirebase
import com.yotfr.randomcats.data.mapper.CatFirebaseMapper
import com.yotfr.randomcats.data.mapper.CatMapper
import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.domain.model.MResult
import com.yotfr.randomcats.domain.repository.CatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatsRepositoryImpl @Inject constructor(
    private val catsApi: CatsApi
) : CatsRepository {

    private val catMapper: CatMapper = CatMapper()
    private val catFirebaseMapper: CatFirebaseMapper = CatFirebaseMapper()

    private val catsCollectionReference = Firebase.firestore.collection("cats")

    override suspend fun getFromApi(): Flow<MResult<Cat>> = flow {
        try {
            emit(MResult.Loading())
            val catsResponse = catsApi.getCat()
            val cat = catMapper.toDomain(catsResponse)
            emit(
                MResult.Success(
                    data = cat
                )
            )
        } catch (e: Exception) {
            emit(
                MResult.Error(
                    message = e.message ?: "Unknown error"
                )
            )
        }
    }

    override suspend fun uploadToRemoteDb(cat: Cat) {
        withContext(Dispatchers.IO) {
            try {
                catsCollectionReference.add(
                    catFirebaseMapper.fromDomain(
                        domainModel = cat
                    )
                ).await()
            } catch (e: Exception) {
                Log.e("uploadError", "error -> ${e.message}")
            }
        }
    }

    override suspend fun getFromRemoteDb(): Flow<MResult<List<Cat>>> = withContext(Dispatchers.IO) {
        catsCollectionReference
            .orderBy("created", Query.Direction.DESCENDING)
            .snapshotFlow()
            .map { querySnapshot ->
            MResult.Success(
                data = querySnapshot.documents.map {
                    it.toObject<CatFirebase>() ?: throw IllegalArgumentException(
                        "Document is null"
                    )
                }.map { catFirebase ->
                    catFirebaseMapper.toDomain(
                        firebaseModel = catFirebase
                    )
                }
            )
        }
    }
}