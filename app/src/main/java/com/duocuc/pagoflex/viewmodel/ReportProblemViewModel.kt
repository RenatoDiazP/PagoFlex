package com.duocuc.pagoflex.viewmodel

import androidx.lifecycle.ViewModel
import com.duocuc.pagoflex.model.ReporteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ReportProblemViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ReporteUiState())

    val uiState: StateFlow<ReporteUiState> = _uiState.asStateFlow()

    fun onTituloCambia(valor: String){
        _uiState.update { it.copy(titulo = valor) }
    }
}