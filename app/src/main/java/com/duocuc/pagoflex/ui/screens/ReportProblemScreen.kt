package com.duocuc.pagoflex.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.duocuc.pagoflex.viewmodel.ReportProblemViewModel

@Composable
fun ReportProblemScreen(
    viewModel: ReportProblemViewModel
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = uiState.titulo,
            onValueChange = viewModel::onTituloCambia,
            label = { Text("Titulo") }
        )

        OutlinedTextField(
            value = uiState.descripcion,
            onValueChange = viewModel::onDescripcionCambia,
            label = { Text("Descripcion") }
        )

        Button(
            onClick = { viewModel.enviarReporte() }
        ) {
            Text("Enviar Reporte")
        }
    }
}