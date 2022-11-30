package com.yotfr.randomcats.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.yotfr.randomcats.domain.model.MResult
import com.yotfr.randomcats.domain.model.SignInModel
import com.yotfr.randomcats.domain.model.SignUpModel
import com.yotfr.randomcats.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : UserRepository {

    override fun signUpUser(signUpModel: SignUpModel): Flow<MResult<Unit>> = flow {
        try {
            emit(MResult.Loading())
            auth.createUserWithEmailAndPassword(signUpModel.email, signUpModel.password).await()
            emit(MResult.Success(Unit))
        } catch (e: Exception) {
            emit(MResult.Error(message = e.message.toString()))
        }
    }

    override fun signInUser(signInModel: SignInModel): Flow<MResult<Unit>> = flow {
        try {
            emit(MResult.Loading())
            auth.signInWithEmailAndPassword(signInModel.email, signInModel.password).await()
            emit(MResult.Success(Unit))
        } catch (e: Exception) {
            emit(MResult.Error(message = e.message.toString()))
        }
    }

    override fun isSignedIn(): Boolean  = auth.currentUser != null

    override fun signOut(): Flow<MResult<Unit>> = flow{
        withContext(Dispatchers.IO) {
            try {
                emit(MResult.Loading())
                auth.currentUser?.apply {
                    delete().await()
                    emit(MResult.Success(Unit))
                }
            } catch (e: Exception) {
                emit(MResult.Error(message = e.message.toString()))
            }
        }
    }
}