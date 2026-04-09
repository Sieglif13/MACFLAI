package com.yey.macflai.network

import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.ResponseBody

interface SinclairApiService {

    @POST("api/generar-desafio")
    suspend fun generarDesafio(): SinclairDto

    @POST("api/retroalimentar")
    suspend fun retroalimentar(
        @Body request: RetroalimentarRequest
    ): ResponseBody

    companion object {
        const val BASE_URL = "http://10.0.2.2:3000/"
    }
}
