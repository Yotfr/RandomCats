package com.yotfr.randomcats.data.data_source

import com.yotfr.randomcats.data.dto.CatResponse
import retrofit2.http.GET

interface CatsApi {
    @GET("cat?json=true")
    suspend fun getCat(): CatResponse
}
