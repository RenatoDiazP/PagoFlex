package com.duocuc.pagoflex.data.local.dao




import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duocuc.pagoflex.data.local.entity.PagoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PagoDao {


    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(pago: PagoEntity): Long


    @Query("""
        SELECT * FROM pago
        ORDER BY fecha_hora DESC
    """)
    fun obtenerTodos(): Flow<List<PagoEntity>>


    @Query("""
        SELECT * FROM pago
        WHERE id_pago = :idPago
        LIMIT 1
    """)
    suspend fun obtenerPorId(idPago: Int): PagoEntity?


    @Query("""
        SELECT * FROM pago
        WHERE id_compromiso = :idCompromiso
        LIMIT 1
    """)
    suspend fun obtenerPorCompromiso(idCompromiso: Int): PagoEntity?


    @Query("""
        SELECT * FROM pago
        WHERE numero_comprobante = :numeroComprobante
        LIMIT 1
    """)
    suspend fun obtenerPorNumeroComprobante(
        numeroComprobante: String
    ): PagoEntity?


    @Query("""
        SELECT * FROM pago
        WHERE fecha_hora BETWEEN :fechaInicio AND :fechaFin
        ORDER BY fecha_hora DESC
    """)
    fun obtenerPorPeriodo(
        fechaInicio: String,
        fechaFin: String
    ): Flow<List<PagoEntity>>

    @Query("""
        SELECT * FROM pago
        WHERE id_medio_pago = :idMedioPago
        ORDER BY fecha_hora DESC
    """)
    fun obtenerPorMedioPago(
        idMedioPago: Int
    ): Flow<List<PagoEntity>>
}
