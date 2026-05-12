package com.example.gestionequipos.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun ListaEquiposScreen(
    viewModel: EquipoViewModel,
    onBack: () -> Unit
) {
    // Observamos equipos y el nuevo mapa de nombres de tipos
    val equipos by viewModel.equipos.collectAsState()
    val nombresTipos by viewModel.nombresTipos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Este bloque nos dirá en Logcat si la pantalla realmente se activa
    LaunchedEffect(Unit) {
        Log.d("DEBUG_LISTA", "¡Pantalla Inventario abierta! Solicitando datos...")
        viewModel.cargarDatosInventario()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventario SQL Server") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.cargarDatosInventario() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (isLoading && equipos.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (equipos.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No hay equipos registrados", color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.cargarDatosInventario() }) {
                        Text("Reintentar")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(equipos) { equipo ->
                        // Buscamos el nombre en el mapa. Si no está, mostramos el ID
                        val descripcionTipo = nombresTipos[equipo.equtipo] ?: "Tipo ${equipo.equtipo}"
                        EquipoCard(equipo, descripcionTipo)
                    }
                }
            }
        }
    }
}

@Composable
fun EquipoCard(equipo: Equipo, nombreTipo: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = equipo.equnumero ?: "SIN NÚMERO",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (equipo.equestado == "A") Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                ) {
                    Text(
                        text = if (equipo.equestado == "A") "ACTIVO" else "BAJA",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (equipo.equestado == "A") Color(0xFF2E7D32) else Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = equipo.equdescripcion ?: "Sin descripción",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Mostramos el nombre del tipo en lugar de solo el ID
            Text(
                text = "Categoría: $nombreTipo",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary
            )

            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Serie: ${equipo.equnumeroserie ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            if (equipo.equetiquetar == true) {
                Text(
                    text = "● Pendiente de etiquetar",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFF57C00),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}