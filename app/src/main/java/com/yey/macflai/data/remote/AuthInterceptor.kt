package com.yey.macflai.data.remote

import com.yey.macflai.domain.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authRepository: AuthRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Recuperar token de forma síncrona en el hilo worker de OkHttp
        val tokenResult = runBlocking {
            authRepository.getAuthToken()
        }

        val requestBuilder = originalRequest.newBuilder()

        // Si existe un token válido, inyectar el header
        tokenResult.onSuccess { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
