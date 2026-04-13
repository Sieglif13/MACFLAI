package com.yey.macflai.domain

import com.yey.macflai.domain.model.User

interface AuthRepository {
    suspend fun getAuthToken(): Result<String>
    fun getCurrentUser(): User?
}
