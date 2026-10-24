package com.duocuc.pagoflex.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class SessionRole {
    USUARIO_FINAL,
    EJECUTIVO,
    ADMINISTRADOR
}

data class SimulatedSession(
    val userId: Int,
    val nombre: String,
    val rut: String,
    val role: SessionRole
)

class SessionViewModel : ViewModel()  {

    private val _session = MutableStateFlow<SimulatedSession?>(null)
    val session: StateFlow<SimulatedSession?> = _session.asStateFlow()


    fun seleccionarSesion(session: SimulatedSession) {
        _session.value = session

    }

    fun cambiarRol(role: SessionRole) {
        val sesionActual = _session.value ?: return

        _session.value = sesionActual.copy(role = role)
    }


    fun cerrarSesion() {
        _session.value = null
    }


}