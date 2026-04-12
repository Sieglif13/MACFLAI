package com.yey.macflai.repository

import android.util.Log
import com.yey.macflai.data.DesafioEntity
import com.yey.macflai.data.MacflaiDao
import com.yey.macflai.network.GenerarDesafioRequest
import com.yey.macflai.network.SinclairApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SinclairRepository(
    private val dao: MacflaiDao,
    private val api: SinclairApiService
) {
    suspend fun getNextChallengeLocal(eje: String): DesafioEntity? {
        return withContext(Dispatchers.IO) {
            dao.getNextDesafio(eje)
        }
    }

    suspend fun markDesafioAsUsed(desafio: DesafioEntity) {
        withContext(Dispatchers.IO) {
            dao.updateDesafio(desafio.copy(esUsado = true))
        }
    }

    // Algoritmo heurístico
    suspend fun calculateDifficulty(eje: String): Int {
        val stats = dao.getUsuarioStats() ?: return 2
        val recentAnswers = stats.ultimasRespuestas
        if (recentAnswers.isEmpty()) return 2

        val correctCount = recentAnswers.count { it }
        val percentage = correctCount.toDouble() / recentAnswers.size

        return when {
            percentage > 0.8 -> 3
            percentage < 0.5 -> 1
            else -> 2
        }
    }

    // Lógica principal de Background Pre-fetching
    suspend fun replenishChallengesIfNeeded(eje: String) {
        withContext(Dispatchers.IO) {
            try {
                val unusedCount = dao.countUnusedDesafios(eje)
                if (unusedCount < 2) {
                    val requiredDiff = calculateDifficulty(eje)
                    val response = api.generarDesafio(
                        GenerarDesafioRequest(eje = eje, dificultad = requiredDiff)
                    )
                    if (response.success && response.data != null) {
                        val dto = response.data
                        val newEntity = DesafioEntity(
                            tema = dto.tema ?: "Tema Dinámico",
                            texto = dto.texto ?: "",
                            pregunta = dto.pregunta ?: "¿?",
                            opciones = dto.opciones ?: emptyMap(),
                            respuestaCorrecta = dto.respuesta_correcta ?: "",
                            eje = eje,
                            dificultad = requiredDiff,
                            esUsado = false
                        )
                        dao.insertDesafio(newEntity)
                    }
                }
            } catch (e: Exception) {
                Log.e("SinclairRepo", "Error pre-fetching para eje $eje", e)
            }
        }
    }

    // Sembrar un quiz inicial con 3 ejes principales
    suspend fun seedInitialQuizIfNeeded() {
        withContext(Dispatchers.IO) {
            val ejesPredefinidos = listOf("Palabras y Significado", "Comprensión Literal", "Comprensión Inferencial")
            for (eje in ejesPredefinidos) {
                replenishChallengesIfNeeded(eje) // Pedirá al menos 1 por cada si faltan
            }
        }
    }
}
