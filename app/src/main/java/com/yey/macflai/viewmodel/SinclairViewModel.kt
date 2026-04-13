package com.yey.macflai.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yey.macflai.data.DesafioEntity
import com.yey.macflai.network.RetroalimentarRequest
import com.yey.macflai.repository.SinclairRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SinclairUiState {
    object Idle : SinclairUiState()
    object CustomLoading : SinclairUiState() 
    data class Success(
        val desafio: DesafioEntity,
        val opcionSeleccionada: String? = null,
        val respuesta: String? = null,
        val esCorrecta: Boolean? = null
    ) : SinclairUiState()
}

@HiltViewModel
class SinclairViewModel @Inject constructor(
    private val repository: SinclairRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SinclairUiState>(SinclairUiState.Idle)
    val uiState: StateFlow<SinclairUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialQuizIfNeeded()
        }
    }

    fun startChallenge(eje: String) {
        _uiState.value = SinclairUiState.CustomLoading
        viewModelScope.launch {
            val localDesafio = repository.getNextChallengeLocal(eje)
            if (localDesafio != null) {
                _uiState.value = SinclairUiState.Success(desafio = localDesafio)
                repository.replenishChallengesIfNeeded(eje)
            } else {
                repository.replenishChallengesIfNeeded(eje)
                val retriedDesafio = repository.getNextChallengeLocal(eje)
                if (retriedDesafio != null) {
                    _uiState.value = SinclairUiState.Success(desafio = retriedDesafio)
                } else {
                    _uiState.value = SinclairUiState.CustomLoading 
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = SinclairUiState.Idle
    }

    fun enviarRespuesta(opcionSeleccionada: String) {
        val currentState = _uiState.value
        if (currentState !is SinclairUiState.Success) return

        val desafioActual = currentState.desafio
        val esCorrecta = opcionSeleccionada == desafioActual.respuestaCorrecta
        
        _uiState.value = currentState.copy(
            opcionSeleccionada = opcionSeleccionada,
            esCorrecta = esCorrecta
        )

        viewModelScope.launch {
            repository.markDesafioAsUsed(desafioActual)
            
            try {
                val request = RetroalimentarRequest(
                    texto = desafioActual.texto,
                    pregunta = desafioActual.pregunta,
                    opcion_usuario = opcionSeleccionada,
                    opcion_correcta = desafioActual.respuestaCorrecta
                )
                val response = repository.retroalimentar(request)
                
                val latestState = _uiState.value
                if (latestState is SinclairUiState.Success) {
                    _uiState.value = latestState.copy(
                        respuesta = response.respuesta ?: "Aprobado sin explicación extensa."
                    )
                }
            } catch (e: Exception) {
                Log.e("SinclairVM", "Feedback error", e)
                val latestState = _uiState.value
                if (latestState is SinclairUiState.Success) {
                    _uiState.value = latestState.copy(
                        respuesta = "Sinclair no pudo darte tu corrección por falta de señal, ¡pero tu progreso ha sido guardado online!"
                    )
                }
            }
        }
    }
}
