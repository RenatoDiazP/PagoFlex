package com.duocuc.pagoflex.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.pagoflex.data.local.AppDatabase
import com.duocuc.pagoflex.data.local.SesionPreferences
import com.duocuc.pagoflex.data.local.entity.ReporteEntity
import com.duocuc.pagoflex.data.repository.ReporteRepository
import com.duocuc.pagoflex.model.EstadoReporte
import com.duocuc.pagoflex.model.ReporteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReportProblemViewModel(application: Application): AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = ReporteRepository(db.reporteDao())
    private val sesion = SesionPreferences(application)
    private val idUsuario = sesion.obtenerUsuarioId()
    private val _uiState = MutableStateFlow(ReporteUiState())

    val uiState: StateFlow<ReporteUiState> = _uiState.asStateFlow()

    fun onTituloCambia(valor: String){
        _uiState.update { it.copy(titulo = valor, errorTitulo = null, reporteEnviado = false) }
    }

    fun onDescripcionCambia(valor: String){
        _uiState.update { it.copy(descripcion = valor, errorDescripcion = null, reporteEnviado = false) }
    }

    fun enviarReporte() {
        val estadoF = _uiState.value

        val errorTituloF = if(estadoF.titulo.isBlank()) "El titulo es obligatorio" else null
        val errorDescripcionF = if(estadoF.descripcion.isBlank()) "La descripcion es obligatoria" else null

        val hayErrores = listOf(errorTituloF, errorDescripcionF).any { it != null }

        _uiState.update { it.copy(errorTitulo = errorTituloF, errorDescripcion =  errorDescripcionF, reporteEnviado = !hayErrores) }

        if(hayErrores) return //termina la funcion si es que hay errores

        viewModelScope.launch {
            val reporte = ReporteEntity(
                usuarioId = idUsuario,
                titulo = estadoF.titulo,
                descripcion = estadoF.descripcion,
                fotoUri = estadoF.imagen,
                estado = EstadoReporte.ABIERTO.name
            )

            repository.crearReporte(reporte)

            _uiState.update { it.copy(reporteEnviado = true) }
        }
    }
}