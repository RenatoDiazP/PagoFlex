package com.duocuc.pagoflex.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recomendacion",
    indices = [
        Index(value = ["id_recomendacion"], unique = true),
        Index(value = ["id_usuario_final"]),

    ],
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario_final"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)


data class RecomendacionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_recomendacion")
    val idRecomendacion: Int = 0,

    @ColumnInfo(name = "numero")
    val numero: String,

    @ColumnInfo(name = "id_usuario_final")
    val idUsuarioFinal: Int,

    @ColumnInfo(name = "nombre_organizacion")
    val nombreOrganizacion: String,

    @ColumnInfo(name = "rut_organizacion")
    val rutOrganizacion: String? = null,

    @ColumnInfo(name = "id_rubro")
    val idRubro: Int,

    @ColumnInfo(name = "id_comuna")
    val idComuna: Int,

    @ColumnInfo(name = "fecha")
    val fecha: String,

    @ColumnInfo(name = "id_estado_recomendacion")
    val idEstadoRecomendacion: Int = 1


)