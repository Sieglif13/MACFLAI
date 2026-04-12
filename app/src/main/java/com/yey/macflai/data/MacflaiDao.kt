package com.yey.macflai.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MacflaiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDesafio(desafio: DesafioEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDesafios(desafios: List<DesafioEntity>)

    @Update
    suspend fun updateDesafio(desafio: DesafioEntity)

    @Query("SELECT * FROM desafios WHERE eje = :eje AND esUsado = 0 ORDER BY id ASC LIMIT 1")
    suspend fun getNextDesafio(eje: String): DesafioEntity?

    @Query("SELECT COUNT(*) FROM desafios WHERE eje = :eje AND esUsado = 0")
    suspend fun countUnusedDesafios(eje: String): Int

    @Query("DELETE FROM desafios WHERE esUsado = 0 AND fechaCreacion < :olderThanTime")
    suspend fun cleanupOldDesafios(olderThanTime: Long)

    @Query("SELECT * FROM usuario_stats WHERE id = 1")
    fun getUsuarioStatsFlow(): Flow<UsuarioStatsEntity?>

    @Query("SELECT * FROM usuario_stats WHERE id = 1")
    suspend fun getUsuarioStats(): UsuarioStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUsuarioStats(stats: UsuarioStatsEntity)
}
