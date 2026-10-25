package com.duocuc.pagoflex.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duocuc.pagoflex.model.EstadoReporte

@Entity(tableName = "reportes")
data class ReporteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_reporte")
    val idReporte: Int = 0,

    @ColumnInfo(name = "id_usuario")
    val usuarioId: Int,

    @ColumnInfo(name = "titulo")
    val titulo: String,

    @ColumnInfo(name = "descripcion")
    val descripcion: String,

    @ColumnInfo(name = "foto_uri")
    val fotoUri: String?,

    @ColumnInfo(name = "estado")
    val estado: String = EstadoReporte.ABIERTO.name,

    val fechaCreacion: Long = System.currentTimeMillis(),

    val respuestaAgente: String? = null,

    val fechaResolucion: Long? = null
)