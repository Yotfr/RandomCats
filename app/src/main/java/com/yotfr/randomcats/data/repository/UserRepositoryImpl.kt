package com.yotfr.randomcats.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.yotfr.randomcats.data.dto.UserFirebase
import com.yotfr.randomcats.data.mapper.UserFirebaseMapper
import com.yotfr.randomcats.domain.model.*
import com.yotfr.randomcats.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Exception

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : UserRepository {

    private val userMapper = UserFirebaseMapper()

    private val usersCollectionReference = fireStore.collection("users")

    // signUp user and create use document in fireStore
    override fun signUpUser(signUpModel: SignUpModel): Flow<Response<Unit, String>> = channelFlow {
        withContext(Dispatchers.IO) {
            try {
                send(Response.Loading)
                auth.createUserWithEmailAndPassword(signUpModel.email, signUpModel.password).await()
                // At this point, the user is successfully created, user cannot be null
                val user = auth.currentUser ?: throw IllegalArgumentException(
                    "User is null"
                )
                val email = user.email ?: throw IllegalArgumentException(
                    "Email is null"
                )
                // create user document reference, ref will be later user in catsRepository
                fireStore.collection("users").document(user.uid).set(
                    UserFirebase(
                        email = email,
                        userName = signUpModel.userName
                    )
                )
                send(Response.Success(Unit))
            } catch (e: Exception) {
                when (e) {
                    is FirebaseAuthUserCollisionException -> {
                        send(
                            Response.Exception(
                                cause = Cause.FirebaseUserAlreadyExistsException
                            )
                        )
                    }
                    // Email validation happens in the viewModel, but "Patterns" validate email not like
                    // firebase requires
                    is FirebaseAuthInvalidCredentialsException -> {
                        send(
                            Response.Exception(
                                cause = Cause.FirebaseEmailBadlyFormattedException
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

    override fun signInUser(signInModel: SignInModel): Flow<Response<Unit, String>> = channelFlow {
        withContext(Dispatchers.IO) {
            try {
                send(Response.Loading)
                auth.signInWithEmailAndPassword(signInModel.email, signInModel.password).await()
                send(Response.Success(Unit))
            } catch (e: Exception) {
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        send(
                            Response.Exception(
                                cause = Cause.InvalidFirebaseCredentialsException
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
                    is FirebaseAuthEmailException -> {
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

    override fun getUserProfileInfo(): Flow<Response<User, String>> = channelFlow {
        send(Response.Loading)
        auth.currentUser?.uid?.let { id ->
            val userDocument = usersCollectionReference
                .document(id).get().await()
            val user = userDocument.toObject<UserFirebase>()
            user?.let { fireBaseUser ->
                send(
                    Response.Success(
                        data = userMapper.toDomain(
                            firebaseModel = fireBaseUser
                        )
                    )
                )
            }
        }
    }

    // This method required to determine which start destination will be used (auth or home)
    override fun isSignedIn(): Boolean = auth.currentUser != null

    override fun signOut(): Flow<Response<Unit, String>> = channelFlow {
        withContext(Dispatchers.IO) {
            try {
                send(Response.Loading)
                auth.signOut()
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

    override fun sendResetPasswordLink(resetPasswordModel: ResetPasswordModel): Flow<Response<Unit, String>> =
        channelFlow {
            withContext(Dispatchers.IO) {
                try {
                    send(Response.Loading)
                    auth.sendPasswordResetEmail(resetPasswordModel.email).await()
                    send(Response.Success(Unit))
                } catch (e: Exception) {
                    when (e) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            send(
                                Response.Exception(
                                    cause = Cause.FirebaseEmailBadlyFormattedException
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
}
