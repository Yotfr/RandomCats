package com.yotfr.randomcats.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.yotfr.randomcats.domain.model.Cause
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.model.SignInModel
import com.yotfr.randomcats.domain.model.SignUpModel
import com.yotfr.randomcats.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : UserRepository {

    override fun signUpUser(signUpModel: SignUpModel): Flow<Response<Unit, String>> = flow {
        try {
            emit(Response.Loading)
            auth.createUserWithEmailAndPassword(signUpModel.email, signUpModel.password).await()
            emit(Response.Success(Unit))
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthUserCollisionException -> {
                    emit(
                        Response.Exception(
                            cause = Cause.FirebaseUserAlreadyExistsException
                        )
                    )
                }
                else -> {
                    emit(
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

    override fun signInUser(signInModel: SignInModel): Flow<Response<Unit, String>> = flow {
        try {
            emit(Response.Loading)
            auth.signInWithEmailAndPassword(signInModel.email, signInModel.password).await()
            emit(Response.Success(Unit))
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    emit(
                        Response.Exception(
                            cause = Cause.InvalidFirebaseCredentialsException
                        )
                    )
                }
                is FirebaseAuthInvalidUserException -> {
                    emit(
                        Response.Exception(
                            cause = Cause.InvalidFirebaseCredentialsException
                        )
                    )
                }
                is FirebaseAuthEmailException -> {
                    emit(
                        Response.Exception(
                            cause = Cause.InvalidFirebaseCredentialsException
                        )
                    )
                }
                else -> {
                    emit(
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

    override fun isSignedIn(): Boolean = auth.currentUser != null

    override fun signOut(): Flow<Response<Unit, String>> = flow {
        withContext(Dispatchers.IO) {
            try {
                emit(Response.Loading)
                auth.currentUser?.apply {
                    delete().await()
                    emit(Response.Success(Unit))
                }
            } catch (e: Exception) {
                emit(
                    Response.Exception(
                        cause = Cause.UnknownException(
                            message = e.message.toString()
                        )
                    )
                )
            }
        }
    }

    override fun getCurrentUserUid(): String = auth.currentUser?.uid ?: throw
            IllegalArgumentException("User is not signed in")

}