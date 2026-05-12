package com.example.gestionequipos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gestionequipos.data.model.Equipo
import com.example.gestionequipos.ui.viewmodel.EquipoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipoFormScreen(
    idDetectado: Int?,
    viewModel: EquipoViewModel,
    onBack: () -> Unit
) {
    var descripcion by remember { mutableStateOf("") }
    var numSerie by remember { mutableStateOf("") }
    var etiquetar by remember { mutableStateOf(true) }

    val tiposDeEquipo = remember {
        listOf(
            1 to "SAI", 2 to "MICROFONO", 3 to "CASCOS TLF.", 4 to "CAMARA", 5 to "ESCANER",
            6 to "ALTAVOCES", 7 to "ROUTER 4G", 8 to "AURICULARES", 9 to "IMPRESORA", 10 to "TARJETA SIM",
            11 to "TECLADO", 12 to "NAS", 13 to "MONITOR", 14 to "TV", 15 to "ORDENADOR SOBREMESA",
            16 to "PORTATIL", 17 to "FUENTE DE ALIMENTACION", 18 to "ROUTER", 19 to "SWITCH", 20 to "TELEFONO",
            21 to "RATON", 22 to "SERVIDOR", 23 to "WIRELESS", 24 to "ADAPTADORES", 25 to "DISPOSITIVOS USB",
            26 to "LECTOR QR/CDB", 27 to "AIRE ACONDICIONADO", 28 to "TABLETS", 29 to "VEHÍCULOS", 30 to "PROYECTOR",
            31 to "ACCESORIOS", 32 to "MOVIL", 33 to "MEMORIA"
        )
    }

    var expanded by remember { mutableStateOf(false) }
    var tipoSeleccionado by remember { mutableStateOf(tiposDeEquipo[15]) } // Por defecto PORTATIL
    var cargando by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Registro") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ID INFO
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("IDENTIFICADOR", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = if (idDetectado == null) "AUTO-GENERADO" else "QR: $idDetectado",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // DESCRIPCIÓN
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (Marca, Modelo...)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Nº SERIE
            OutlinedTextField(
                value = numSerie,
                onValueChange = { numSerie = it },
                label = { Text("Número de Serie") },
                modifier = Modifier.fillMaxWidth()
            )

            // SELECTOR DE TIPO
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = tipoSeleccionado.second,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Equipo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    tiposDeEquipo.forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo.second) },
                            onClick = {
                                tipoSeleccionado = tipo
                                expanded = false
                            }
                        )
                    }
                }
            }

            // ETIQUETAR
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = etiquetar, onCheckedChange = { etiquetar = it })
                Text("¿Marcar para etiquetar?")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    cargando = true
                    val equipo = Equipo(
                        equequipo = idDetectado,
                        equdescripcion = descripcion,
                        equnumeroserie = numSerie,
                        equtipo = tipoSeleccionado.first,
                        equestado = "A",
                        equetiquetar = etiquetar
                    )
                    viewModel.crearEquipo(equipo) { exito ->
                        cargando = false
                        if (exito) onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = descripcion.isNotBlank() && !cargando
            ) {
                if (cargando) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Icon(Icons.Default.Check, null)
                    Spacer(Modifier.width(8.dp))
                    Text("GUARDAR EQUIPO")
                }
            }
        }
    }
}