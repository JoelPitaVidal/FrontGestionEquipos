package com.example.gestionequipos.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gestionequipos.ui.viewmodel.EquipoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: EquipoViewModel, // AÑADIDO: Necesitamos el viewModel aquí
    onScanClick: () -> Unit,
    onViewClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Gestión de Equipos",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top, // Cambiado a Top para que no se amontone todo en el centro
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Panel de Control",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Botón 1: Escanear
            MenuButton(
                text = "Escanear",
                icon = Icons.Default.Search,
                onClick = onScanClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón 2: Ver Equipos
            MenuButton(
                text = "Ver Equipos",
                icon = Icons.Default.List,
                onClick = onViewClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón 3: Editar Equipos
            MenuButton(
                text = "Editar Equipos",
                icon = Icons.Default.Edit,
                onClick = onEditClick
            )

            // --- ESTO ES LO QUE FALTABA ---
            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón de sincronizar hacia abajo

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            BotonSincronizacion(viewModel = viewModel)
            // ------------------------------
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp), // Ajustado un poco la altura
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BotonSincronizacion(viewModel: EquipoViewModel) {
    val context = LocalContext.current

    Button(
        onClick = {
            viewModel.iniciarSincronizacion(
                onSuccess = { count ->
                    Toast.makeText(context, "¡Éxito! $count equipos enviados", Toast.LENGTH_LONG).show()
                },
                onError = { error ->
                    Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
                }
            )
        },
        enabled = !viewModel.cargandoSync,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        if (viewModel.cargandoSync) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
            Spacer(Modifier.width(12.dp))
            Text("Sincronizando...")
        } else {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(Modifier.width(12.dp))
            Text("Sincronizar con Servidor", fontWeight = FontWeight.Bold)
        }
    }
}