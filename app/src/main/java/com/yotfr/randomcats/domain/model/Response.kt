package com.yotfr.randomcats.domain.model

sealed class Response<out S, out F>{
    object Loading : Response<Nothing, Nothing>()
    data class Success<S>(val data: S): Response<S,Nothing>()
    data class Error<F>(val error:F):Response<Nothing,F>()
    data class Exception(val cause: Cause):Response<Nothing,Nothing>()
}
