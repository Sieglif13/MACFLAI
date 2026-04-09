package com.yey.macflai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yey.macflai.network.RetroalimentarRequest
import com.yey.macflai.network.SinclairDto
import com.yey.macflai.network.SinclairNetwork
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SinclairUiState {
    object Idle : SinclairUiState()
    object Loading : SinclairUiState()
    data class Success(
        val desafio: SinclairDto,
        val opcionSeleccionada: String? = null,
        val explicacion: String? = null,
        val esCorrecta: Boolean? = null
    ) : SinclairUiState()
    data class Error(val message: String) : SinclairUiState()
}

class SinclairViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<SinclairUiState>(SinclairUiState.Idle)
    val uiState: StateFlow<SinclairUiState> = _uiState.asStateFlow()

    private val api = SinclairNetwork.api

    fun generarNuevoDesafio() {
        _uiState.value = SinclairUiState.Loading

        viewModelScope.launch {
            try {
                val desafio = api.generarDesafio()
                _uiState.value = SinclairUiState.Success(desafio = desafio)
            } catch (e: Exception) {
                _uiState.value = SinclairUiState.Error(
                    message = "¡Ups! No pudimos conectar con Sinclair. Verifica que el servidor (ngrok) esté encendido y tengas internet."
                )
            }
        }
    }

    fun enviarRespuesta(opcionSeleccionada: String) {
        val currentState = _uiState.value
        if (currentState !is SinclairUiState.Success) return

        val desafioActual = currentState.desafio
        
        // Comparamos si la opción es la correcta a nivel local (aprovechando que ya lo sabemos)
        val esCorrecta = opcionSeleccionada == desafioActual.respuesta_correcta
        
        // Actualizamos estado parcialmente (mostramos selección) mientras responde la retroalimentación
        _uiState.value = currentState.copy(
            opcionSeleccionada = opcionSeleccionada,
            esCorrecta = esCorrecta
        )

        viewModelScope.launch {
            try {
                val request = RetroalimentarRequest(
                    texto = desafioActual.texto,
                    pregunta = desafioActual.pregunta,
                    opcion_usuario = opcionSeleccionada,
                    opcion_correcta = desafioActual.respuesta_correcta
                )
                val response = api.retroalimentar(request)
                
                // Actualizamos estado actual (Success) con la respuesta de Sinclair
                val latestState = _uiState.value
                if (latestState is SinclairUiState.Success) {
                    _uiState.value = latestState.copy(
                        explicacion = response.explicacion
                    )
                }
            } catch (e: Exception) {
                // Notificar error de conexión sin perder el state local de ser posible,
                // O transicionar a error global, según la preferencia.
                _uiState.value = SinclairUiState.Error(
                    message = "La conexión falló al enviar la respuesta. Revisa si Sinclair está despierto."
                )
            }
        }
    }
}
