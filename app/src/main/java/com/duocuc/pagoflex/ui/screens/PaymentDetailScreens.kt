package com.duocuc.pagoflex.ui.screens




import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duocuc.pagoflex.data.local.entity.CompromisoEntity
import com.duocuc.pagoflex.viewmodel.PaymentViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PaymentDetailScreen(
    compromiso: CompromisoEntity,
    paymentViewModel: PaymentViewModel,
    onPagoExitoso: () -> Unit,
    onVolver: () -> Unit
) {
    val estado by paymentViewModel.estado.collectAsState()
    var medioPagoSeleccionado by remember {
        mutableIntStateOf(1)
    }

    val formatoPesos = NumberFormat.getNumberInstance(
        Locale("es", "CL")
    )

    val total = compromiso.monto + compromiso.recargoAplicado

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Detalle del compromiso",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = compromiso.concepto,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    DetalleFila(
                        etiqueta = "Folio",
                        valor = compromiso.folio
                    )

                    DetalleFila(
                        etiqueta = "Vencimiento",
                        valor = compromiso.fechaVencimiento
                    )

                    DetalleFila(
                        etiqueta = "Monto original",
                        valor = "$ ${formatoPesos.format(compromiso.monto)}"
                    )

                    DetalleFila(
                        etiqueta = "Recargo aplicado",
                        valor = "$ ${
                            formatoPesos.format(compromiso.recargoAplicado)
                        }"
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    DetalleFila(
                        etiqueta = "Total a pagar",
                        valor = "$ ${formatoPesos.format(total)}",
                        destacado = true
                    )
                }
            }

            Text(
                text = "Medio de pago",
                style = MaterialTheme.typography.titleMedium
            )

            // Opciones ilustrativas para el entorno de prueba.
            listOf(
                1 to "Tarjeta",
                2 to "Transferencia",
                3 to "Otro medio"
            ).forEach { (id, nombre) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = medioPagoSeleccionado == id,
                        onClick = { medioPagoSeleccionado = id },
                        enabled = !estado.procesando && !estado.pagoExitoso
                    )

                    Text(
                        text = nombre,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            if (estado.procesando) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            if (estado.mensaje.isNotBlank()) {
                Text(
                    text = estado.mensaje,
                    color = if (estado.pagoExitoso) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (estado.pagoExitoso) {
                estado.numeroComprobante?.let { numero ->
                    Text(
                        text = "Comprobante: $numero",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Button(
                    onClick = onPagoExitoso,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continuar")
                }
            } else {
                Button(
                    onClick = {
                        paymentViewModel.pagarCompromiso(
                            idCompromiso = compromiso.idCompromiso,
                            idMedioPago = medioPagoSeleccionado
                        )
                    },
                    enabled = !estado.procesando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pagar monto completo")
                }
            }

            OutlinedButton(
                onClick = onVolver,
                enabled = !estado.procesando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}

@Composable
private fun DetalleFila(
    etiqueta: String,
    valor: String,
    destacado: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = valor,
            style = if (destacado) {
                MaterialTheme.typography.titleMedium
            } else {
                MaterialTheme.typography.bodyLarge
            },
            fontWeight = if (destacado) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            color = if (destacado) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}
