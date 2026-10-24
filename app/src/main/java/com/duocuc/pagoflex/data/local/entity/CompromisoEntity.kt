package com.duocuc.pagoflex.data.local.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "compromisos",
    indices = [
        Index(value = ["folio"], unique = true),
        Index(value = ["id_usuario_final"]),
        Index(value = ["id_empresa"])

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
data class CompromisoEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_compromiso")
    val idCompromiso: Int = 0,

    @ColumnInfo(name = "folio")
    val folio: String,

    @ColumnInfo(name = "id_usuario_final")
    val idUsuarioFinal: Int,

    @ColumnInfo(name = "id_empresa")
    val idEmpresa: Int,

    @ColumnInfo(name = "concepto")
    val concepto: String,

     @ColumnInfo(name = "numero_cuota")
    val numeroCuota: Int? = null,

    @ColumnInfo(name = "total_cuotas")
    val totalCuotas: Int? = null,

    @ColumnInfo(name = "monto")
    val monto: Int,

    @ColumnInfo(name = "fecha_vencimiento")
    val fechaVencimiento: String,

    @ColumnInfo(name = "id_estado_compromiso")
    val idEstadoCompromiso: Int,

    @ColumnInfo(name = "recargo_aplicado")
    val recargoAplicado: Int = 0,

    @ColumnInfo(name = "fecha_cambio_estado")
    val fechaCambioEstado: String,

    @ColumnInfo(name = "id_ejecutivo_registro")
    val idEjecutivoRegistro: Int? = null,

     @ColumnInfo(name = "fecha_registro")
    val fechaRegistro: String

)
