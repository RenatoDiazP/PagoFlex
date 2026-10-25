package com.duocuc.pagoflex.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.duocuc.pagoflex.data.local.dao.AnulacionDao
import com.duocuc.pagoflex.data.local.dao.CompromisoDao
import com.duocuc.pagoflex.data.local.dao.EmpresaClienteDao
import com.duocuc.pagoflex.data.local.dao.PagoDao
import com.duocuc.pagoflex.data.local.dao.RecomendacionDao
import com.duocuc.pagoflex.data.local.dao.ReporteDao
import com.duocuc.pagoflex.data.local.dao.UsuarioDao
import com.duocuc.pagoflex.data.local.entity.AnulacionEntity
import com.duocuc.pagoflex.data.local.entity.CompromisoEntity
import com.duocuc.pagoflex.data.local.entity.EmpresaClienteEntity
import com.duocuc.pagoflex.data.local.entity.PagoEntity
import com.duocuc.pagoflex.data.local.entity.RecomendacionEntity
import com.duocuc.pagoflex.data.local.entity.ReporteEntity
import com.duocuc.pagoflex.data.local.entity.UsuarioEntity

@Database(
    entities = [
        ReporteEntity::class,
        CompromisoEntity::class,
        EmpresaClienteEntity::class,
        AnulacionEntity::class,
        UsuarioEntity::class,
        PagoEntity::class,
        RecomendacionEntity::class,
    ],
    version = 2
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun reporteDao(): ReporteDao
    abstract fun compromisoDao(): CompromisoDao
    abstract fun anulacionDao(): AnulacionDao
    abstract fun empresaDao(): EmpresaClienteDao
    abstract fun pagoDao(): PagoDao
    abstract fun recomendacionDao(): RecomendacionDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pagoflex_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}