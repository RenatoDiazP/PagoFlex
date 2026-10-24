package com.duocuc.pagoflex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.duocuc.pagoflex.data.local.dao.ReporteDao
import com.duocuc.pagoflex.data.local.entity.AnulacionEntity
import com.duocuc.pagoflex.data.local.entity.CompromisoEntity
import com.duocuc.pagoflex.data.local.entity.EmpresaClienteEntity
import com.duocuc.pagoflex.data.local.entity.ReporteEntity

@Database(
    entities = [
        ReporteEntity::class,
        CompromisoEntity::class,
        EmpresaClienteEntity::class,
        AnulacionEntity::class,
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun reporteDao(): ReporteDao
    // abstract fun compromisoDao(): CompromisoDao
    abstract fun anulacionDao(): AnulacionDao
}