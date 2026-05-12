package com.example.gestionequipos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestionequipos.data.local.Entity.EquipoEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroEquipoScreen(
    idEscaneado: Int,
    numeroEscaneado: String,
    onGuardarClick: (EquipoEntity) -> Unit,
    onCancelar: () -> Unit
) {
    var descripcion by remember { mutableStateOf("") }
    var serie by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("A") }
    var tipo by remember { mutableStateOf("1") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Confirmar Registro") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Mostramos los datos detectados para que el usuario verifique
            OutlinedTextField(
                value = idEscaneado.toString(),
                onValueChange = {},
                label = { Text("ID Detectado (EQUEQUIPO)") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = numeroEscaneado,
                onValueChange = {},
                label = { Text("Nº Inventario (EQUNUMERO)") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción *") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = serie, onValueChange = { serie = it }, label = { Text("Nº Serie *") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado (A/B) *") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = tipo, onValueChange = { tipo = it }, label = { Text("Tipo ID *") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // CONSTRUCCIÓN DEL OBJETO FINAL
                    onGuardarClick(EquipoEntity(
                        id = idEscaneado,           // 223
                        numero = numeroEscaneado,   // "0000223"
                        descripcion = descripcion,
                        serie = serie,
                        estado = estado,
                        tipo = tipo.toIntOrNull() ?: 1,
                        etiquetar = true,
                        sincronizado = false        // IMPORTANTE: Empezar en false para que el botón de Sync lo vea
                    ))
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = descripcion.isNotBlank() && serie.isNotBlank()
            ) {
                Text("Guardar Localmente")
            }

            TextButton(onClick = onCancelar, modifier = Modifier.fillMaxWidth()) {
                Text("Cancelar", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}