package com.duocuc.pagoflex.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.duocuc.pagoflex.data.local.entity.EmpresaClienteEntity

@Dao
interface   EmpresaClienteDao {
    @Query("SELECT * FROM empresa_cliente WHERE id = :empresaId")
    suspend fun obtenerEmpresa(empresaId: Int): EmpresaClienteEntity?
}