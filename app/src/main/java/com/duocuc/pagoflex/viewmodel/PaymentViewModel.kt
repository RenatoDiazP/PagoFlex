package com.duocuc.pagoflex.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.pagoflex.data.local.dao.CompromisoDao
import com.duocuc.pagoflex.data.local.dao.PagoDao
import com.duocuc.pagoflex.data.local.entity.PagoEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

data class PaymentState(
    val procesando: Boolean = false,
    val pagoExitoso: Boolean = false,
    val mensaje: String = "",
    val numeroComprobante: String? = null
)

class PaymentViewModel(
    private val compromisoDao: CompromisoDao,
    private val pagoDao: PagoDao
) : ViewModel() {

    private val _estado = MutableStateFlow(PaymentState())
    val estado: StateFlow<PaymentState> = _estado.asStateFlow()
    val historialPagos: StateFlow<List<PagoEntity>> =
        pagoDao.obtenerTodos()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun pagarCompromiso(
        idCompromiso: Int,
        idMedioPago: Int
    ) {
        viewModelScope.launch {
            _estado.value = PaymentState(
                procesando = true,
                mensaje = "Procesando pago..."
            )

            try {
                val compromiso =
                    compromisoDao.obtenerPorId(idCompromiso)

                if (compromiso == null) {
                    _estado.value = PaymentState(
                        mensaje = "No se encontró el compromiso."
                    )
                    return@launch
                }

                // ID 3 es provisional: confirmar cuál representa Pagado.
                if (compromiso.idEstadoCompromiso == 3) {
                    _estado.value = PaymentState(
                        mensaje = "Este compromiso ya está pagado."
                    )
                    return@launch
                }

                val pagoExistente =
                    pagoDao.obtenerPorCompromiso(idCompromiso)

                if (pagoExistente != null) {
                    _estado.value = PaymentState(
                        mensaje = "Este compromiso ya tiene un pago registrado."
                    )
                    return@launch
                }


                val compromisosEmpresa =
                    compromisoDao.obtenerPorEmpresa(
                        compromiso.idEmpresa
                    ).first()


                val estadosPorPagar = listOf(1, 2)

                val compromisoMasAntiguo = compromisosEmpresa
                    .filter {
                        it.idEstadoCompromiso in estadosPorPagar
                    }
                    .minByOrNull { it.fechaVencimiento }

                if (compromisoMasAntiguo?.idCompromiso != idCompromiso) {
                    _estado.value = PaymentState(
                        mensaje = "Debes pagar primero el compromiso más antiguo de esta empresa."
                    )
                    return@launch
                }

                val numeroComprobante =
                    "PF-${UUID.randomUUID().toString().take(8).uppercase()}"

                val fechaHora = java.text.SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss",
                    java.util.Locale.getDefault()
                ).format(java.util.Date())

                val pago = PagoEntity(
                    numeroComprobante = numeroComprobante,
                    idCompromiso = compromiso.idCompromiso,
                    fechaHora = fechaHora,
                    montoPagado = compromiso.monto +
                            compromiso.recargoAplicado,
                    idMedioPago = idMedioPago
                )

                pagoDao.insertar(pago)



                compromisoDao.actualizar(
                    id = compromiso.idCompromiso,
                    estado = 3,
                    fecha = fechaHora
                )


                _estado.value = PaymentState(
                    pagoExitoso = true,
                    mensaje = "Pago registrado correctamente.",
                    numeroComprobante = numeroComprobante
                )

            } catch (e: Exception) {
                _estado.value = PaymentState(
                    mensaje = "No se pudo registrar el pago. Inténtalo nuevamente."
                )
            }
        }
    }

    fun limpiarEstado() {
        _estado.value = PaymentState()
    }
}
