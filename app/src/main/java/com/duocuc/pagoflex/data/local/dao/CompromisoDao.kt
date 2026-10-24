package com.duocuc.pagoflex.data.local.dao


    import androidx.room.Dao
    import androidx.room.Insert
    import androidx.room.OnConflictStrategy
    import androidx.room.Query
    import androidx.room.Update
    import com.duocuc.pagoflex.data.local.entity.CompromisoEntity
    import kotlinx.coroutines.flow.Flow

    @Dao
    interface CompromisoDao {


        @Insert(onConflict = OnConflictStrategy.ABORT)
        suspend fun insertar(compromiso: CompromisoEntity): Long


        @Query("SELECT * FROM compromisos ORDER BY fecha_vencimiento ASC")
        fun obtenerTodos(): Flow<List<CompromisoEntity>>


        @Query("""
        SELECT * FROM compromisos
        WHERE id_usuario_final = :idUsuario
        ORDER BY fecha_vencimiento ASC
    """)
        fun obtenerPorUsuario(idUsuario: Int): Flow<List<CompromisoEntity>>

        @Query("""
        SELECT * FROM compromisos
        WHERE id_empresa = :idEmpresa
        ORDER BY fecha_vencimiento ASC
    """)
        fun obtenerPorEmpresa(idEmpresa: Int): Flow<List<CompromisoEntity>>


        @Query("""
        SELECT * FROM compromisos
        WHERE id_estado_compromiso = :idEstado
        ORDER BY fecha_vencimiento ASC
    """)
        fun obtenerPorEstado(idEstado: Int): Flow<List<CompromisoEntity>>


        @Query("""
        SELECT * FROM compromisos
        WHERE id_compromiso = :idCompromiso
        LIMIT 1
    """)
        suspend fun obtenerPorId(idCompromiso: Int): CompromisoEntity?


        @Update
        suspend fun actualizar(compromiso: CompromisoEntity): Int


        @Query("""
        DELETE FROM compromisos
        WHERE id_compromiso = :idCompromiso
    """)
        suspend fun eliminarPorId(idCompromiso: Int): Int
    }

