package com.duocuc.pagoflex.data.repository

import com.duocuc.pagoflex.data.local.dao.ReporteDao
import com.duocuc.pagoflex.data.local.entity.ReporteEntity

class ReporteRepository(
    private val dao: ReporteDao
) {
    fun obtenerReportes() = dao.obtenerReportes()

    suspend fun obtenerReportePorId(id: Int) = dao.obtenerPorId(id)

    suspend fun crearReporte(reporte: ReporteEntity){
        dao.insertarReporte(reporte)
    }

    suspend fun tomarReporte(id: Int){
        dao.tomarReporte(id)
    }

    fun obtenerReportesPorEstado(estado: String) = dao.obtenerPorEstado(estado)

    suspend fun resolverReporte(id: Int, respuesta: String){
        require(respuesta.isNotBlank()){
            "Debe ingresar una respuesta"
        }

        dao.resolverReporte(id, respuesta)
    }

    fun obtenerAtrasados(limite: Long) = dao.obtenerReportesAtrasados(limite)
}