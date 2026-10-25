package com.duocuc.pagoflex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.duocuc.pagoflex.data.local.entity.AnulacionEntity

@Dao
interface AnulacionDao{
    @Insert
    suspend fun registrarAnulacion(anulacion: AnulacionEntity)

    @Query("SELECT * FROM anulaciones WHERE id_compromiso = :id")
    suspend fun obtenerPorCompromiso(id: Int): List<AnulacionEntity>
}