package com.yey.macflai.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario_stats")
data class UsuarioStatsEntity(
    @PrimaryKey val id: Int = 1,
    val progresoPorEje: Map<String, Int>, 
    val rachaActual: Int = 0,
    val nivel: String = "Novato",
    val ultimasRespuestas: List<Boolean> = emptyList() 
)
