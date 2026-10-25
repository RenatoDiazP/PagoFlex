package com.duocuc.pagoflex.navigation

sealed class Ruta(val ruta: String){
    data object ReportarProblema: Ruta("reportar_problema")

    data object ReportesAgente: Ruta("reportes_agente")

    data object DashboardEjecutivo: Ruta("dashboard_ejecutivo")

    data object RegistrarCompromiso: Ruta("registrar_compromiso")
}