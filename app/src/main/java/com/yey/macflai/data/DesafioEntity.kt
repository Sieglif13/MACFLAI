package com.yey.macflai.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "desafios")
data class DesafioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tema: String,
    val texto: String,
    val pregunta: String,
    val opciones: Map<String, String>, 
    val respuestaCorrecta: String,
    val eje: String,
    val dificultad: Int,
    val esUsado: Boolean = false,
    val fechaCreacion: Long = System.currentTimeMillis()
)
