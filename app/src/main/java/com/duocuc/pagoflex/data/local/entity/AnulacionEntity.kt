package com.duocuc.pagoflex.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anulaciones")
class AnulacionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_anulacion")
    val id:Int = 0,

    @ColumnInfo(name = "id_compromiso")
    val compromisoId:Int,

    @ColumnInfo(name = "motivo")
    val motivo:String,

    @ColumnInfo(name = "id_usuario")
    val usuario:String,

    @ColumnInfo(name = "fecha_anulacion")
    val fecha:Long = System.currentTimeMillis()
)