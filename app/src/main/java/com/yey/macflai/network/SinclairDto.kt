package com.yey.macflai.network

import com.google.gson.annotations.SerializedName

data class SinclairDto(
    @SerializedName("tema") val tema: String? = null,
    @SerializedName("texto") val texto: String? = null,
    @SerializedName("pregunta") val pregunta: String? = null,
    @SerializedName("opciones") val opciones: Map<String, String>? = emptyMap(),
    @SerializedName("respuesta_correcta") val respuesta_correcta: String? = null,
    @SerializedName("tiempo_respuesta") val tiempo_respuesta: String? = "0"
)

data class SinclairApiResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("data") val data: SinclairDto? = null
)

data class GenerarDesafioRequest(
    @SerializedName("eje") val eje: String,
    @SerializedName("dificultad") val dificultad: Int
)

data class RetroalimentarRequest(
    @SerializedName("texto") val texto: String?,
    @SerializedName("pregunta") val pregunta: String?,
    @SerializedName("opcion_usuario") val opcion_usuario: String?,
    @SerializedName("opcion_correcta") val opcion_correcta: String?
)

data class SinclairFeedbackResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("respuesta") val respuesta: String? = null
)
