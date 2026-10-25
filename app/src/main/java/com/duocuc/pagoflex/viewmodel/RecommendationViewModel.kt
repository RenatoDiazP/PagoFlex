package com.duocuc.pagoflex.viewmodel




import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.pagoflex.data.local.dao.RecomendacionDao
import com.duocuc.pagoflex.data.local.entity.RecomendacionEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

data class RecommendationState(
    val procesando: Boolean = false,
    val mensaje: String = "",
    val operacionExitosa: Boolean = false
)

class RecommendationViewModel(
    private val recomendacionDao: RecomendacionDao
) : ViewModel() {

    // Lista reactiva de recomendaciones.
    val recomendaciones: StateFlow<List<RecomendacionEntity>> =
        recomendacionDao.obtenerTodas()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // Estado de las operaciones.
    private val _estado = MutableStateFlow(RecommendationState())
    val estado: StateFlow<RecommendationState> =
        _estado.asStateFlow()

    // Registrar una recomendación.
    fun crearRecomendacion(
        idUsuarioFinal: Int,
        nombreOrganizacion: String,
        rutOrganizacion: String?,
        idRubro: Int,
        idComuna: Int
    ) {
        if (nombreOrganizacion.isBlank()) {
            _estado.value = RecommendationState(
                mensaje = "Ingresa el nombre de la organización."
            )
            return
        }

        if (idUsuarioFinal <= 0 || idRubro <= 0 || idComuna <= 0) {
            _estado.value = RecommendationState(
                mensaje = "Verifica los datos de la recomendación."
            )
            return
        }

        viewModelScope.launch {
            _estado.value = RecommendationState(
                procesando = true,
                mensaje = "Registrando recomendación..."
            )

            try {
                val recomendacion = RecomendacionEntity(
                    numero = "REC-${UUID.randomUUID()
                        .toString().take(8).uppercase()}",
                    idUsuarioFinal = idUsuarioFinal,
                    nombreOrganizacion = nombreOrganizacion.trim(),
                    rutOrganizacion = rutOrganizacion
                        ?.trim()
                        ?.takeIf { it.isNotEmpty() },
                    idRubro = idRubro,
                    idComuna = idComuna,
                    fecha = java.text.SimpleDateFormat(
                        "yyyy-MM-dd",
                        java.util.Locale.getDefault()
                    ).format(java.util.Date()),
                    idEstadoRecomendacion = 1
                )

                recomendacionDao.insertar(recomendacion)

                _estado.value = RecommendationState(
                    operacionExitosa = true,
                    mensaje = "Recomendación registrada correctamente."
                )
            } catch (e: Exception) {
                _estado.value = RecommendationState(
                    mensaje = "No se pudo registrar la recomendación."
                )
            }
        }
    }

    // Cambiar el estado de una recomendación.
    fun cambiarEstado(
        idRecomendacion: Int,
        nuevoEstado: Int
    ) {
        if (idRecomendacion <= 0 || nuevoEstado <= 0) {
            _estado.value = RecommendationState(
                mensaje = "Los datos del estado no son válidos."
            )
            return
        }

        viewModelScope.launch {
            _estado.value = RecommendationState(
                procesando = true,
                mensaje = "Actualizando estado..."
            )

            try {
                val filasActualizadas =
                    recomendacionDao.cambiarEstado(
                        idRecomendacion,
                        nuevoEstado
                    )

                _estado.value = RecommendationState(
                    operacionExitosa = filasActualizadas > 0,
                    mensaje = if (filasActualizadas > 0) {
                        "Estado actualizado correctamente."
                    } else {
                        "No se encontró la recomendación."
                    }
                )
            } catch (e: Exception) {
                _estado.value = RecommendationState(
                    mensaje = "No se pudo actualizar el estado."
                )
            }
        }
    }


    fun obtenerPorUsuario(idUsuario: Int) =
        recomendacionDao.obtenerPorUsuario(idUsuario)

    // Consultar recomendaciones según su estado.
    fun obtenerPorEstado(idEstado: Int) =
        recomendacionDao.obtenerPorEstado(idEstado)

    fun limpiarEstado() {
        _estado.value = RecommendationState()
    }
}
