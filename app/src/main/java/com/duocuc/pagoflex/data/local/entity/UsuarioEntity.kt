package com.duocuc.pagoflex.data.local.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "usuarios",
    indices = [
        Index(value = ["rut"], unique = true),
        Index(value = ["correo"], unique = true)


    ]
)
data class UsuarioEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int = 0,

    @ColumnInfo(name = "id_rol")
    val idRol: Int,

    @ColumnInfo(name = "nombres")
    val nombres: String,

    @ColumnInfo(name = "apellidos")
    val apellidos: String,

    @ColumnInfo(name = "correo")
    val correo: String,

    @ColumnInfo(name = "telefono")
    val telefono: String? = null,

    @ColumnInfo(name = "activo")
    val activo: Boolean = true,

    @ColumnInfo(name = "fecha_creacion")
    val fechaCreacion: String,

    @ColumnInfo(name = "id_comuna")
    val idComuna: Int? = null,

    @ColumnInfo(name = "fecha_incorporacion")
    val fechaIncorporacion: String? = null,

    @ColumnInfo(name = "avisos_activos")
    val avisosActivos: Boolean? = true,

    @ColumnInfo(name = "dias_anticipacion")
    val diasAnticipacion: Int? = 3,

    @ColumnInfo(name = "id_area")
    val idArea: Int? = null,

    @ColumnInfo(name = "id_empresa")
    val idEmpresa: Int? = null,

    @ColumnInfo(name = "cargo")
    val cargo: String? = null



)