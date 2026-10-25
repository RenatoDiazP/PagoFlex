package com.duocuc.pagoflex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.duocuc.pagoflex.data.local.entity.ReporteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteDao {
    @Insert
    suspend fun insertarReporte(reporte: ReporteEntity): Long

    @Query("SELECT * FROM reportes ORDER BY fechaCreacion DESC")
    fun obtenerReportes(): Flow<List<ReporteEntity>>

    @Query("SELECT * FROM reportes WHERE estado = :estado ORDER BY fechaCreacion DESC")
    fun obtenerPorEstado(estado: String): Flow<List<ReporteEntity>>

    @Query("SELECT * FROM reportes WHERE id = :id")
    suspend fun obtenerPorId(id: Int): ReporteEntity?

    @Query("UPDATE reportes SET estado = 'En Gestion' WHERE id = :id")
    suspend fun tomarReporte(id: Int)

    @Query("""UPDATE reportes 
        SET estado = 'Resuelto',
            respuestaAgente = :respuesta,
            fechaResolucion = :fecha
        WHERE id = :id
    """)
    suspend fun resolverReporte(id:Int, respuesta:String, fecha: Long = System.currentTimeMillis())

    @Query("SELECT * FROM reportes WHERE estado != 'Resuelto' AND fechaCreacion < :limite")
    fun obtenerReportesAtrasados(limite: Long): Flow<List<ReporteEntity>>
}