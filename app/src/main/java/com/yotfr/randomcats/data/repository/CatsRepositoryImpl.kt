package com.yotfr.randomcats.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.yotfr.randomcats.base.snapshotFlow
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
import javax.inject.Singleton

@Singleton
class CatsRepositoryImpl @Inject constructor(
    private val catsApi: CatsApi,
    private val fireStore: FirebaseFirestore,
    auth: FirebaseAuth
) : CatsRepository {

    private val catMapper: CatMapper = CatMapper()
    private val catFirebaseMapper: CatFirebaseMapper = CatFirebaseMapper()

    // Current logged in user collection of cats
    private val catsCollectionReference = auth.currentUser?.let {
        fireStore.collection("users").document(
            it.uid
        ).collection("cats")
    }

    // get random car from api
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
            when (e) {
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

    // upload cat to user collection of cats
    override suspend fun uploadToRemoteDb(cat: Cat): Flow<Response<Unit, String>> =
        channelFlow {
            withContext(Dispatchers.IO) {
                // if catsCollection reference is null, the current user is null
                // But this shouldn't happen because non authenticated user only have access to auth
                // graph screens, null check here for the unexpected cases
                if (catsCollectionReference == null) {
                    // The user will be redirected to the auth screen
                    send(
                        Response.Exception(
                            cause = Cause.UserIsNotLoggedIn
                        )
                    )
                    return@withContext
                }
                // Case the user is logged in
                try {
                    send(Response.Loading)
                    // Add doc to fireStore collection
                    catsCollectionReference.add(
                        catFirebaseMapper.fromDomain(
                            domainModel = cat
                        )
                    ).await()
                    send(Response.Success(Unit))
                } catch (e: Exception) {
                    // TODO
                    Log.e("uploadError", "error -> ${e.message}")
                }
            }
        }

    override suspend fun getFromRemoteDb(): Flow<Response<List<Cat>, String>> =
        withContext(Dispatchers.IO) {
            // TODO
            catsCollectionReference!!
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

    override suspend fun deleteFromRemoteDb(cat: Cat) =
        withContext(Dispatchers.IO) {
            val firebaseCat = catFirebaseMapper.fromDomain(
                domainModel = cat
            )
            val catQuery = catsCollectionReference!!
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
