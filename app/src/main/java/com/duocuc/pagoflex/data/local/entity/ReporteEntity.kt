package com.duocuc.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reportes")
data class ReporteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val usuarioId: Int,
    val titulo: String,
    val descripcion: String,

    val fotoUri: String?,

    val estado: String = "Abierto",

    val fechaCreacion: Long = System.currentTimeMillis(),

    val respuestaAgente: String? = null
)