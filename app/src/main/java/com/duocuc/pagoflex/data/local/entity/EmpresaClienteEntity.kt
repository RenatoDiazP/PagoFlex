package com.duocuc.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "empresa_cliente")
class EmpresaClienteEntity (
    @PrimaryKey
    val id: Int,
    val nombre: String,
    val rut: String
)