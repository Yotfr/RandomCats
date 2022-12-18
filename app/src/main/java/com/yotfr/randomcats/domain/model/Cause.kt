package com.yotfr.randomcats.domain.model

sealed interface Cause {
    object BadConnectionException : Cause
    object InvalidFirebaseCredentialsException : Cause
    object FirebaseUserAlreadyExistsException : Cause
    object FirebaseEmailBadlyFormattedException : Cause
    object UserIsNotLoggedIn : Cause
    data class UnknownException(val message: String) : Cause
}
