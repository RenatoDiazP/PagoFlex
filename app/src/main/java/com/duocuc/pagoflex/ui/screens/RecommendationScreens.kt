package com.duocuc.pagoflex.ui.screens




import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duocuc.pagoflex.data.local.entity.RecomendacionEntity
import com.duocuc.pagoflex.viewmodel.RecommendationViewModel

@Composable
fun RecommendationScreen(
    usuarioId: Int,
    recommendationViewModel: RecommendationViewModel,
    onVolver: () -> Unit
) {
    var nombreOrganizacion by remember { mutableStateOf("") }
    var rutOrganizacion by remember { mutableStateOf("") }
    var idRubroTexto by remember { mutableStateOf("") }
    var idComunaTexto by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val recomendaciones by recommendationViewModel.recomendaciones
        .collectAsState()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextButton(onClick = onVolver) {
                    Text("Volver")
                }

                Text(
                    text = "Recomendaciones",
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Recomienda una organización",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Ayúdanos a incorporar nuevas organizaciones a PagoFlex.",
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = nombreOrganizacion,
                onValueChange = { nombreOrganizacion = it },
                label = { Text("Nombre de la organización") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = rutOrganizacion,
                onValueChange = { rutOrganizacion = it },
                label = { Text("RUT de la organización (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = idRubroTexto,
                onValueChange = { idRubroTexto = it },
                label = { Text("ID del rubro") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = idComunaTexto,
                onValueChange = { idComunaTexto = it },
                label = { Text("ID de la comuna") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    val idRubro = idRubroTexto.toIntOrNull()
                    val idComuna = idComunaTexto.toIntOrNull()

                    when {
                        nombreOrganizacion.isBlank() ->
                            mensaje = "Ingresa el nombre de la organización."

                        idRubro == null || idRubro <= 0 ->
                            mensaje = "Ingresa un ID de rubro válido."

                        idComuna == null || idComuna <= 0 ->
                            mensaje = "Ingresa un ID de comuna válido."

                        else -> {
                            recommendationViewModel.crearRecomendacion(
                                idUsuarioFinal = usuarioId,
                                nombreOrganizacion = nombreOrganizacion.trim(),
                                rutOrganizacion = rutOrganizacion
                                    .trim()
                                    .ifBlank { null },
                                idRubro = idRubro,
                                idComuna = idComuna
                            )

                            mensaje = "Solicitud de recomendación enviada."
                            nombreOrganizacion = ""
                            rutOrganizacion = ""
                            idRubroTexto = ""
                            idComunaTexto = ""
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar recomendación")
            }

            if (mensaje.isNotBlank()) {
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Mis recomendaciones",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            val misRecomendaciones = recomendaciones.filter {
                it.idUsuarioFinal == usuarioId
            }

            if (misRecomendaciones.isEmpty()) {
                Text("Todavía no tienes recomendaciones registradas.")
            } else {
                misRecomendaciones.forEach { recomendacion ->
                    RecommendationCard(recomendacion)
                }
            }
        }
    }
}

@Composable
private fun RecommendationCard(
    recomendacion: RecomendacionEntity
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = recomendacion.nombreOrganizacion,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text("Número: ${recomendacion.numero}")
            Text("Fecha: ${recomendacion.fecha}")
            Text("Estado: ${nombreEstado(recomendacion.idEstadoRecomendacion)}")
        }
    }
}

private fun nombreEstado(idEstado: Int): String {
    return when (idEstado) {
        1 -> "Recibida"
        2 -> "En revisión"
        3 -> "Aceptada"
        4 -> "Rechazada"
        else -> "Estado pendiente de definir"
    }
}
