package com.yotfr.randomcats.data.datasource

import com.yotfr.randomcats.data.dto.CatResponse
import retrofit2.http.GET

interface CatsApi {
    @GET("cat?json=true")
    suspend fun getCat(): CatResponse
}
