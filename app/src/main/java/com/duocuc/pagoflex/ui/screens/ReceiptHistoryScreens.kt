package com.duocuc.pagoflex.ui.screens




import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duocuc.pagoflex.data.local.entity.PagoEntity
import com.duocuc.pagoflex.viewmodel.PaymentViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ReceiptHistoryScreen(
    paymentViewModel: PaymentViewModel,
    onVolver: () -> Unit
) {
    val estado by paymentViewModel.estado.collectAsState()
    val pagos by paymentViewModel.historialPagos.collectAsState()

    val formatoPesos = NumberFormat.getNumberInstance(Locale("es", "CL"))

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onVolver) {
                    Text("Volver")
                }

                Text(
                    text = "Historial de pagos",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Consulta tus comprobantes cuando los necesites.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.padding(8.dp))

            if (estado.procesando) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else if (pagos.isEmpty()) {
                Text(
                    text = "Todavía no tienes pagos registrados.",
                    modifier = Modifier.padding(top = 16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = pagos,
                        key = { it.idPago }
                    ) { pago ->
                        ReceiptCard(
                            pago = pago,
                            formatoPesos = formatoPesos
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptCard(
    pago: PagoEntity,
    formatoPesos: NumberFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Comprobante ${pago.numeroComprobante}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text("Fecha: ${pago.fechaHora}")

            Text(
                text = "Monto pagado: $ ${formatoPesos.format(pago.montoPagado)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text("Medio de pago: ${nombreMedioPago(pago.idMedioPago)}")
        }
    }
}

private fun nombreMedioPago(idMedioPago: Int): String {
    return when (idMedioPago) {
        1 -> "Tarjeta"
        2 -> "Transferencia"
        3 -> "Otro medio"
        else -> "No especificado"
    }
}
