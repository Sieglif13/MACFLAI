package com.yey.macflai.data

import com.google.firebase.auth.FirebaseAuth
import com.yey.macflai.domain.AuthRepository
import com.yey.macflai.domain.model.User
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class FirebaseAuthRepository(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun getAuthToken(): Result<String> {
        return try {
            val user = auth.currentUser
            if (user != null) {
                // Force refresh false, retrieves id token securely
                val tokenResult = user.getIdToken(false).await()
                val token = tokenResult.token
                
                if (token != null) {
                    Result.success(token)
                } else {
                    Result.failure(Exception("Hubo un error obteniendo el token de Firebase."))
                }
            } else {
                Result.failure(Exception("El usuario no está autenticado."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): User? {
        val user = auth.currentUser ?: return null
        return User(
            uid = user.uid,
            email = user.email,
            displayName = user.displayName
        )
    }

    override suspend fun signInWithGoogleCredential(idToken: String): Result<User> {
        return try {
            val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            val user = authResult.user
            if (user != null) {
                Result.success(
                    User(
                        uid = user.uid,
                        email = user.email,
                        displayName = user.displayName
                    )
                )
            } else {
                Result.failure(Exception("Hubo un error validando la credencial"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
