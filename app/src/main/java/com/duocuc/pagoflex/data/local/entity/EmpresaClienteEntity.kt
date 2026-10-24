package com.duocuc.pagoflex.data.local.entity

import androidx.room.PrimaryKey

class EmpresaClienteEntity (
    @PrimaryKey
    val id: Int,
    val nombre: String,
    val rut: String
)