package com.duocuc.pagoflex.model

enum class EstadoReporte{
    ABIERTO,
    EN_GESTION,
    RESUELTO
}

data class ReporteUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val imagen: String? = null,
    val estado: EstadoReporte = EstadoReporte.ABIERTO,
    val error: String? = null
)