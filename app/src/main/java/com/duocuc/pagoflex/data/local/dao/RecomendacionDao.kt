package com.duocuc.pagoflex.data.local.dao



import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.duocuc.pagoflex.data.local.entity.RecomendacionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecomendacionDao {

    // Registrar una recomendación
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(recomendacion: RecomendacionEntity): Long

    // Obtener todas las recomendaciones
    @Query("""
        SELECT * FROM recomendacion
        ORDER BY fecha DESC
    """)
    fun obtenerTodas(): Flow<List<RecomendacionEntity>>

    // Buscar recomendación por ID
    @Query("""
        SELECT * FROM recomendacion
        WHERE id_recomendacion = :idRecomendacion
        LIMIT 1
    """)
    suspend fun obtenerPorId(
        idRecomendacion: Int
    ): RecomendacionEntity?

    // Obtener recomendaciones de un usuario
    @Query("""
        SELECT * FROM recomendacion
        WHERE id_usuario_final = :idUsuario
        ORDER BY fecha DESC
    """)
    fun obtenerPorUsuario(
        idUsuario: Int
    ): Flow<List<RecomendacionEntity>>

    // Buscar recomendaciones por estado
    @Query("""
        SELECT * FROM recomendacion
        WHERE id_estado_recomendacion = :idEstado
        ORDER BY fecha DESC
    """)
    fun obtenerPorEstado(
        idEstado: Int
    ): Flow<List<RecomendacionEntity>>

    // Buscar una organización por su RUT
    @Query("""
        SELECT * FROM recomendacion
        WHERE rut_organizacion = :rutOrganizacion
        ORDER BY fecha DESC
    """)
    fun obtenerPorRutOrganizacion(
        rutOrganizacion: String
    ): Flow<List<RecomendacionEntity>>

    // Actualizar una recomendación
    @Update
    suspend fun actualizar(
        recomendacion: RecomendacionEntity
    ): Int

    // Cambiar el estado de una recomendación
    @Query("""
        UPDATE recomendacion
        SET id_estado_recomendacion = :idEstado
        WHERE id_recomendacion = :idRecomendacion
    """)
    suspend fun cambiarEstado(
        idRecomendacion: Int,
        idEstado: Int
    ): Int

    // Eliminar una recomendación por ID
    @Query("""
        DELETE FROM recomendacion
        WHERE id_recomendacion = :idRecomendacion
    """)
    suspend fun eliminarPorId(
        idRecomendacion: Int
    ): Int
}
