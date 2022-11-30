package com.yotfr.randomcats.domain.repository

import com.yotfr.randomcats.domain.model.MResult
import com.yotfr.randomcats.domain.model.SignInModel
import com.yotfr.randomcats.domain.model.SignUpModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun isSignedIn():Boolean

    fun signOut(): Flow<MResult<Unit>>

    fun signUpUser(signUpModel: SignUpModel): Flow<MResult<Unit>>

    fun signInUser(signInModel: SignInModel): Flow<MResult<Unit>>

}