package com.yotfr.randomcats.domain.model

sealed class MResult<T>(val data:T? = null, val message:String? = null){
    class Loading<T>:MResult<T>()
    class Success<T>(data: T?): MResult<T>(data)
    class Error<T>(message:String, data: T? = null):MResult<T>(data, message)
}
