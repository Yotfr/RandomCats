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
import com.yotfr.randomcats.domain.model.Cause
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.repository.CatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class CatsRepositoryImpl @Inject constructor(
    private val catsApi: CatsApi
) : CatsRepository {

    private val catMapper: CatMapper = CatMapper()
    private val catFirebaseMapper: CatFirebaseMapper = CatFirebaseMapper()

    private val catsCollectionReference = Firebase.firestore.collection("cats")

    override suspend fun getFromApi(): Flow<Response<Cat, String>> = channelFlow {
        try {
            send(Response.Loading)
            val catsResponse = catsApi.getCat()
            val cat = catMapper.toDomain(catsResponse)
            send(
                Response.Success(
                    data = cat
                )
            )
        } catch (e: Exception) {
            when(e) {
                is IOException -> {
                    send(
                        Response.Exception(
                            cause = Cause.BadConnectionException
                        )
                    )
                }
                else -> {
                    send(
                        Response.Exception(
                            cause = Cause.UnknownException(
                                message = e.message.toString()
                            )
                        )

                    )
                }
            }
        }
    }

    override suspend fun uploadToRemoteDb(cat: Cat, userId: String): Flow<Response<Unit, String>> = channelFlow{
        withContext(Dispatchers.IO) {
            try {
                send(Response.Loading)
                catsCollectionReference.add(
                    catFirebaseMapper.fromDomain(
                        domainModel = cat,
                        userId = userId
                    )
                ).await()
                send(Response.Success(Unit))
            } catch (e: Exception) {
                Log.e("uploadError", "error -> ${e.message}")
            }
        }
    }

    override suspend fun getFromRemoteDb(userId: String): Flow<Response<List<Cat>, String>> =
        withContext(Dispatchers.IO) {
            catsCollectionReference
                .whereEqualTo("userId",userId)
                .orderBy("created", Query.Direction.DESCENDING)
                .snapshotFlow()
                .map { querySnapshot ->
                    Response.Success(
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

    override suspend fun deleteFromRemoteDb(cat: Cat, userId: String) = withContext(Dispatchers.IO) {
        val firebaseCat = catFirebaseMapper.fromDomain(
            domainModel = cat,
            userId = userId
        )
        val catQuery = catsCollectionReference
            .whereEqualTo("id", firebaseCat.id)
            .whereEqualTo("url", firebaseCat.url)
            .whereEqualTo("created", firebaseCat.created)
            .get()
            .await()
        if (catQuery.documents.isNotEmpty()) {
            for (document in catQuery) {
                try {
                    catsCollectionReference.document(document.id).delete().await()
                } catch (e: Exception) {
                    Log.e("uploadError", "error -> ${e.message}")
                }
            }
        }
    }
}