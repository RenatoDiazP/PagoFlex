package com.duocuc.pagoflex.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "empresa_cliente")
class EmpresaClienteEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_empresa")
    val id: Int,

    @ColumnInfo(name = "nombre")
    val nombre: String,

    @ColumnInfo(name = "rut")
    val rut: String
)