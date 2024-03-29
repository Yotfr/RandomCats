package com.yotfr.randomcats.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObjects
import com.yotfr.randomcats.data.datasource.CatsApi
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

    /**
     * [catsCollectionReference] is a collection of cats of current logged-in user
     */
    private val catsCollectionReference = auth.currentUser?.let {
        fireStore.collection("users").document(
            it.uid
        ).collection("cats")
    }

    /**
     * [getFromApi] function gets random cat from an Api
     */
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
                /**
                 * Other type of exceptions not handled here because an Api only return success
                 * result with code 200
                 */
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

    /**
     * [uploadToRemoteDb] method upload cat to firestore user collection of cats
     */
    override suspend fun uploadToRemoteDb(cat: Cat): Flow<Response<Unit, String>> =
        channelFlow {
            withContext(Dispatchers.IO) {
                /**
                 * If catsCollection reference is null, the current user is null
                 * But this shouldn't happen because non authenticated user only have access to auth
                 * graph screens, null check here for the unexpected cases
                 */
                if (catsCollectionReference == null) {
                    /**
                     * The user will be redirected to the auth screen
                     */
                    send(
                        Response.Exception(
                            cause = Cause.UserIsNotLoggedIn
                        )
                    )
                    return@withContext
                }
                /**
                 * Case the user is logged in
                 */
                try {
                    send(Response.Loading)
                    /**
                     * Add cat document to fireStore collection
                     */
                    catsCollectionReference.add(
                        catFirebaseMapper.fromDomain(
                            domainModel = cat
                        )
                    ).await()
                    send(Response.Success(Unit))
                } catch (e: Exception) {
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

    override suspend fun getFromRemoteDb(): Flow<Response<List<Cat>, String>> = channelFlow {
        withContext(Dispatchers.IO) {
            send(Response.Loading)
            /**
             * If catsCollection reference is null, the current user is null
             * But this shouldn't happen because non authenticated user only have access to auth
             * graph screens, null check here for the unexpected cases
             */
            if (catsCollectionReference == null) {
                /**
                 * The user will be redirected to the auth screen
                 */
                send(
                    Response.Exception(
                        cause = Cause.UserIsNotLoggedIn
                    )
                )
                return@withContext
            }
            /**
             * Case the user is logged in
             */
            catsCollectionReference
                .orderBy("created", Query.Direction.DESCENDING)
                .snapshots().map {
                    it.toObjects<CatFirebase>()
                }
                .map { catsFirebase ->
                    catFirebaseMapper.toDomainList(
                        initial = catsFirebase
                    )
                }.collectLatest { cats ->
                    send(
                        Response.Success(
                            data = cats
                        )
                    )
                }
        }
    }

    /**
     * [deleteFromRemoteDb] method deletes cats document from a firestore user collection of cats
     */
    override suspend fun deleteFromRemoteDb(cat: Cat): Flow<Response<Unit, String>> = channelFlow {
        withContext(Dispatchers.IO) {
            send(Response.Loading)
            /**
             * If catsCollection reference is null, the current user is null
             * But this shouldn't happen because non authenticated user only have access to auth
             * graph screens, null check here for the unexpected cases
             */
            if (catsCollectionReference == null) {
                /**
                 * The user will be redirected to the auth screen
                 */
                send(
                    Response.Exception(
                        cause = Cause.UserIsNotLoggedIn
                    )
                )
                return@withContext
            }
            /**
             * Case the user is logged in
             */
            val firebaseCat = catFirebaseMapper.fromDomain(
                domainModel = cat
            )
            val catQuery = catsCollectionReference
                .whereEqualTo("url", firebaseCat.url)
                .whereEqualTo("created", firebaseCat.created)
                .get()
                .await()
            if (catQuery.documents.isNotEmpty()) {
                for (document in catQuery) {
                    try {
                        catsCollectionReference.document(document.id).delete().await()
                        send(
                            Response.Success(Unit)
                        )
                    } catch (e: Exception) {
                        send(
                            Response.Exception(
                                cause = Cause.UnknownException(e.message.toString())
                            )
                        )
                    }
                }
            }
        }
    }
}
