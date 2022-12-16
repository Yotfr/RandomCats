package com.yotfr.randomcats.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.yotfr.randomcats.domain.model.*
import com.yotfr.randomcats.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Exception

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
                is FirebaseAuthInvalidCredentialsException -> {
                    emit(
                        Response.Exception(
                            cause = Cause.FirebaseEmailBadlyFromattedException
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

    override fun signOut(): Flow<Response<Unit, String>> = channelFlow {
        withContext(Dispatchers.IO) {
            try {
                Log.d("TEST", "trying")
                send(Response.Loading)
                auth.signOut()
                send(Response.Success(Unit))
            } catch (e: Exception) {
                Log.d("TEST", "exception ${e.message} ${e.cause} $e")
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

    override fun sendResetPasswordLink(resetPasswordModel: ResetPasswordModel): Flow<Response<Unit, String>> = channelFlow {
        withContext(Dispatchers.IO) {
            try {
                send(Response.Loading)
                auth.sendPasswordResetEmail(resetPasswordModel.email).await()
                send(Response.Success(Unit))
            } catch (e: Exception) {
                Log.d("TEST","exc -> ${e.cause} ${e.message} $e")
                when(e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        send(
                            Response.Exception(
                                cause = Cause.FirebaseEmailBadlyFromattedException
                            )
                        )
                    }
                    is FirebaseAuthInvalidUserException -> {
                        send(
                            Response.Exception(
                                cause = Cause.InvalidFirebaseCredentialsException
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
    }

    override fun getCurrentUserUid(): String = auth.currentUser?.uid ?: throw
    IllegalArgumentException("User is not signed in")
}
