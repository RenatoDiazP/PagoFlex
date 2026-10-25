package com.duocuc.pagoflex.viewmodel




import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.pagoflex.data.local.dao.CompromisoDao
import com.duocuc.pagoflex.data.local.entity.CompromisoEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

data class DashboardState(
    val totalPorPagar: Int = 0,
    val proximaFechaVencimiento: String? = null,
    val cantidadVencidos: Int = 0,
    val compromisos: List<CompromisoEntity> = emptyList()
)

class UserDashboardViewModel(
    compromisoDao: CompromisoDao,
    private val usuarioId: Int
) : ViewModel() {

    val dashboard: StateFlow<DashboardState> =
        compromisoDao.obtenerPorUsuario(usuarioId)
            .map { compromisos ->

                val hoy = java.text.SimpleDateFormat(
                    "yyyy-MM-dd",
                    java.util.Locale.getDefault()
                ).format(java.util.Date())

                val pendientes = compromisos.filter {
                    it.idEstadoCompromiso == 1
                }

                val vencidos = compromisos.filter {
                    it.idEstadoCompromiso == 2
                }

                DashboardState(
                    totalPorPagar = pendientes.sumOf { it.monto },
                    proximaFechaVencimiento = pendientes
                        .filter {
                            it.fechaVencimiento >= hoy
                        }
                        .minByOrNull { it.fechaVencimiento }
                        ?.fechaVencimiento,
                    cantidadVencidos = vencidos.size,
                    compromisos = compromisos
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = DashboardState()
            )
}
