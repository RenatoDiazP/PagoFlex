package com.duocuc.pagoflex.navigation

sealed class Ruta(val ruta: String){
    data object ReportarProblema: Ruta("reportar_problema")

    data object ReportesAgente: Ruta("reportes_agente")

    data object DashboardEjecutivo: Ruta("dashboard_ejecutivo")

    data object RegistrarCompromiso: Ruta("registrar_compromiso")

    data object UserHome: Ruta("user_home")

    data object ReceiptHistory: Ruta("receipts")

    data object PaymentDetail: Ruta("payment_detail/{id}"){
        fun crear(id: Int) = "payment_detail/$id"
    }

    data object Recomendaciones: Ruta("recomendaciones")

    data object SessionSelect: Ruta("session_select")
}