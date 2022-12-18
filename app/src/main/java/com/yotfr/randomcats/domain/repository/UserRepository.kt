package com.yotfr.randomcats.domain.repository

import com.yotfr.randomcats.domain.model.*
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun isSignedIn(): Boolean

    fun signOut(): Flow<Response<Unit, String>>

    fun signUpUser(signUpModel: SignUpModel): Flow<Response<Unit, String>>

    fun signInUser(signInModel: SignInModel): Flow<Response<Unit, String>>

    fun sendResetPasswordLink(resetPasswordModel: ResetPasswordModel): Flow<Response<Unit, String>>

    fun getUserProfileInfo(): Flow<Response<User, String>>
}
