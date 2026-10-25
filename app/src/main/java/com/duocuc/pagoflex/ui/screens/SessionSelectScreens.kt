package com.duocuc.pagoflex.ui.screens




import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duocuc.pagoflex.viewmodel.SessionRole
import com.duocuc.pagoflex.viewmodel.SessionViewModel
import com.duocuc.pagoflex.viewmodel.SimulatedSession

@Composable
fun SessionSelectScreen(
    sessionViewModel: SessionViewModel,
    onSesionSeleccionada: () -> Unit
) {
    var userId by remember { mutableStateOf("1") }
    var nombre by remember { mutableStateOf("Usuario de prueba") }
    var rut by remember { mutableStateOf("15.*.*-5") }

    var rolSeleccionado by remember {
        mutableStateOf(SessionRole.USUARIO_FINAL)
    }

    val idValido = userId.toIntOrNull()?.let { it > 0 } == true
    val formularioValido =
        idValido && nombre.isNotBlank() && rut.isNotBlank()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "PagoFlex",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tus compromisos, más simples.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Seleccionar sesión",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = userId,
                        onValueChange = { userId = it },
                        label = { Text("ID de usuario") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = rut,
                        onValueChange = { rut = it },
                        label = { Text("RUT enmascarado") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Rol de prueba",
                        style = MaterialTheme.typography.titleMedium
                    )

                    SessionRole.values().forEach { rol ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = rolSeleccionado == rol,
                                onClick = { rolSeleccionado = rol }
                            )

                            Text(
                                text = when (rol) {
                                    SessionRole.USUARIO_FINAL ->
                                        "Usuario final"

                                    SessionRole.EJECUTIVO ->
                                        "Ejecutivo"

                                    SessionRole.ADMINISTRADOR ->
                                        "Administrador"
                                },
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val id = userId.toIntOrNull()

                            if (id != null &&
                                id > 0 &&
                                nombre.isNotBlank() &&
                                rut.isNotBlank()
                            ) {
                                sessionViewModel.seleccionarSesion(
                                    SimulatedSession(
                                        userId = id,
                                        nombre = nombre.trim(),
                                        rut = rut.trim(),
                                        role = rolSeleccionado
                                    )
                                )

                                onSesionSeleccionada()
                            }
                        },
                        enabled = formularioValido,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ingresar")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = {
                            userId = "1"
                            nombre = "Usuario de prueba"
                            rut = "15.*.*-5"
                            rolSeleccionado = SessionRole.USUARIO_FINAL
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Restablecer")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Entorno de demostración",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
                    .copy(alpha = 0.7f)
            )
        }
    }
}
