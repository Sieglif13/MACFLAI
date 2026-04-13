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

