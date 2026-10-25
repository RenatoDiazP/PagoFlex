package com.duocuc.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anulaciones")
class AnulacionEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,

    val compromisoId:Int,
    val motivo:String,
    val usuario:String,
    val fecha:Long = System.currentTimeMillis()
)