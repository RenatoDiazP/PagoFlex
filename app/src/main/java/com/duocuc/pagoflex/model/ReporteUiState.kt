package com.duocuc.pagoflex.model

data class ReporteUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val imagen: String? = null,
    val errorTitulo: String? = null,
    val errorDescripcion: String? = null,
    val reporteEnviado: Boolean = false
)