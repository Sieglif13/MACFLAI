package com.yey.macflai.network

data class SinclairDto(
    val tema: String,
    val texto: String,
    val pregunta: String,
    val opciones: Map<String, String>,
    val respuesta_correcta: String,
    val tiempo_respuesta: String
)

data class RetroalimentarRequest(
    val texto: String,
    val pregunta: String,
    val opcion_usuario: String,
    val opcion_correcta: String
)

data class RetroalimentarResponse(
    val explicacion: String
)
