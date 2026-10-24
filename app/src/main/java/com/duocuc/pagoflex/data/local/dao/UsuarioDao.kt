package com.duocuc.pagoflex.data.local.dao




import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.duocuc.pagoflex.data.local.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {


    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(usuario: UsuarioEntity): Long

    @Query("SELECT * FROM usuarios ORDER BY nombres ASC")
    fun obtenerTodos(): Flow<List<UsuarioEntity>>


    @Query("""
        SELECT * FROM usuarios
        WHERE id_usuario = :idUsuario
        LIMIT 1
    """)
    suspend fun obtenerPorId(idUsuario: Int): UsuarioEntity?


    @Query("""
        SELECT * FROM usuarios
        WHERE rut = :rut
        LIMIT 1
    """)
    suspend fun obtenerPorRut(rut: String): UsuarioEntity?


    @Query("""
        SELECT * FROM usuarios
        WHERE correo = :correo
        LIMIT 1
    """)
    suspend fun obtenerPorCorreo(correo: String): UsuarioEntity?


    @Query("""
        SELECT * FROM usuarios
        WHERE id_rol = :idRol
        ORDER BY nombres ASC
    """)
    fun obtenerPorRol(idRol: Int): Flow<List<UsuarioEntity>>


    @Query("""
        SELECT * FROM usuarios
        WHERE activo = 1
        ORDER BY nombres ASC
    """)
    fun obtenerActivos(): Flow<List<UsuarioEntity>>


    @Update
    suspend fun actualizar(usuario: UsuarioEntity): Int

    @Query("""
        UPDATE usuarios
        SET activo = :activo
        WHERE id_usuario = :idUsuario
    """)
    suspend fun cambiarEstado(idUsuario: Int, activo: Boolean): Int


    @Query("""
        DELETE FROM usuarios
        WHERE id_usuario = :idUsuario
    """)
    suspend fun eliminarPorId(idUsuario: Int): Int
}
