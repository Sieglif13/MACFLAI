package com.yey.macflai.network

import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface SinclairApiService {

    @POST("api/generar-desafio")
    suspend fun generarDesafio(): SinclairDto

    @POST("api/retroalimentar")
    suspend fun retroalimentar(
        @Body request: RetroalimentarRequest
    ): RetroalimentarResponse

    companion object {
        const val BASE_URL = "http://10.0.2.2:3000/"
    }
}

object SinclairNetwork {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SinclairApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: SinclairApiService by lazy {
        retrofit.create(SinclairApiService::class.java)
    }
}
