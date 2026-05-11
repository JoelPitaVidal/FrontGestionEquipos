package com.example.gestionequipos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.gestionequipos.ui.screens.HomeScreen
import com.example.gestionequipos.ui.screens.ScannerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation()
                }
            }
        }
    }
}

@Composable
fun MainNavigation() {
    var currentScreen by remember { mutableStateOf("home") }
    var showDialog by remember { mutableStateOf(false) } // Controla si se ve el pop-up
    var scannedText by remember { mutableStateOf("") }  // Guarda el código leído
    val context = LocalContext.current

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            currentScreen = "scanner"
        } else {
            Toast.makeText(context, "Se necesita permiso de cámara", Toast.LENGTH_SHORT).show()
        }
    }

    // --- LÓGICA DEL POP-UP (DIALOG) ---
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Código Detectado", fontWeight = FontWeight.Bold) },
            text = {
                Text(text = "Se ha leído el siguiente contenido:\n\n$scannedText")
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Aceptar")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    when (currentScreen) {
        "home" -> HomeScreen(
            onScanClick = {
                val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    currentScreen = "scanner"
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            onViewClick = { /* Próximamente */ },
            onEditClick = { /* Próximamente */ }
        )
        "scanner" -> ScannerScreen(onCodeScanned = { codigoCompleto ->
            // 1. Extraemos la parte numérica final: "0000223"
            val numeroEquipo = codigoCompleto.substringAfterLast('-')

            // 2. Convertimos a entero para quitar los ceros: 223
            // toIntOrNull() es más seguro por si el código tuviera letras por error
            val idEquipo = numeroEquipo.toIntOrNull() ?: 0

            // 3. Guardamos los datos para el pop-up (o para enviarlos luego)
            scannedText = "Nº Equipo: $numeroEquipo\nID Equipo : $idEquipo"

            showDialog = true
            currentScreen = "home"
        })
    }
}