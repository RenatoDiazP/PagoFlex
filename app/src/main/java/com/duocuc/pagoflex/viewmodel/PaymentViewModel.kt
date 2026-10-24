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

                // Validar que sea el compromiso más antiguo
                // pendiente o vencido de la misma empresa.
                val compromisosEmpresa =
                    compromisoDao.obtenerPorEmpresa(
                        compromiso.idEmpresa
                    ).first()

                // IDs provisionales: 1 = Pendiente, 2 = Vencido.
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

                val fechaHora = LocalDateTime.now().format(
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                )

                val pago = PagoEntity(
                    numeroComprobante = numeroComprobante,
                    idCompromiso = compromiso.idCompromiso,
                    fechaHora = fechaHora,
                    montoPagado = compromiso.monto +
                            compromiso.recargoAplicado,
                    idMedioPago = idMedioPago
                )

                pagoDao.insertar(pago)

                // ID 3 provisional: estado Pagado.
                compromisoDao.actualizar(
                    compromiso.copy(
                        idEstadoCompromiso = 3,
                        fechaCambioEstado = fechaHora
                    )
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
