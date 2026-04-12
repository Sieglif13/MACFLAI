package com.yey.macflai.network

import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface SinclairApiService {

    @POST("api/generar-desafio")
    suspend fun generarDesafio(
        @Body request: GenerarDesafioRequest
    ): SinclairApiResponse

    @POST("api/retroalimentar")
    suspend fun retroalimentar(
        @Body request: RetroalimentarRequest
    ): SinclairFeedbackResponse

    companion object {
        const val BASE_URL = "https://tricrotic-nontumorous-twanna.ngrok-free.dev/"
    }
}

object SinclairNetwork {
    private val client by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SinclairApiService.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: SinclairApiService by lazy {
        retrofit.create(SinclairApiService::class.java)
    }
}
