package com.duocuc.pagoflex.data.local.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pago",
    indices = [
        Index(value = ["numero_comprobante"], unique = true),
        Index(value = ["id_compromiso"], unique = true),
    ],
    foreignKeys = [
        ForeignKey(
            entity = CompromisoEntity::class,
            parentColumns = ["id_compromiso"],
            childColumns = ["id_compromiso"],
            onDelete = ForeignKey.RESTRICT
        )
    ]

)

data class PagoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_pago")
    val idPago: Int = 0,

    @ColumnInfo(name = "numero_comprobante")
    val numeroComprobante: String,

    @ColumnInfo(name = "id_compromiso")
    val idCompromiso: Int,

    @ColumnInfo(name = "fecha_hora")
    val fechaHora: String,

    @ColumnInfo(name = "monto_pagado")
    val montoPagado: Int,

    @ColumnInfo(name = "id_medio_pago")
    val idMedioPago: Int



)