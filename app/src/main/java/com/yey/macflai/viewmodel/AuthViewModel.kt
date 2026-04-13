package com.yey.macflai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yey.macflai.domain.AuthRepository
import com.yey.macflai.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.io.IOException
import javax.inject.Inject

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: User) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
    object Offline : AuthUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        verifyCurrentUser()
    }

    fun verifyCurrentUser() {
        val user = authRepository.getCurrentUser()
        if (user != null) {
            _uiState.value = AuthUiState.Success(user)
        } else {
            _uiState.value = AuthUiState.Idle
        }
    }

    fun signInWithGoogle(idToken: String) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            val result = authRepository.signInWithGoogleCredential(idToken)
            result.onSuccess { user ->
                _uiState.value = AuthUiState.Success(user)
            }.onFailure { exception ->
                // Clasificando errores de desconexión sin Android Context
                val isNetworkError = exception is UnknownHostException || 
                                     exception is IOException ||
                                     exception.message?.contains("network", true) == true
                
                if (isNetworkError) {
                    _uiState.value = AuthUiState.Offline
                } else {
                    _uiState.value = AuthUiState.Error(exception.message ?: "Error desconocido en el servidor Auth")
                }
            }
        }
    }
    
    fun resetState() {
        if (_uiState.value !is AuthUiState.Success) {
            _uiState.value = AuthUiState.Idle
        }
    }
}
