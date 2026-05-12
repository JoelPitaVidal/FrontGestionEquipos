package com.example.gestionequipos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestionequipos.ui.screens.*
import com.example.gestionequipos.ui.viewmodel.EquipoViewModel
import com.example.gestionequipos.data.remote.RetrofitClient
import com.example.gestionequipos.data.repository.EquipoRepository


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val apiService = RetrofitClient.apiService
// 1. Creamos el repositorio
            val repository = EquipoRepository(apiService)
// 2. Pasamos el repositorio al ViewModel
            val viewModel: EquipoViewModel = viewModel { EquipoViewModel(repository) }

            MainNavigation(viewModel)
        }
    }
}

@Composable
fun MainNavigation(viewModel: EquipoViewModel) {
    var currentScreen by remember { mutableStateOf("home") }
    val context = LocalContext.current

    // Estado del permiso de cámara
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    // ID del equipo: null para Registro Manual, valor Int para escaneo QR
    var idDetectado by remember { mutableStateOf<Int?>(null) }

    // Lanzador para solicitar permisos de cámara en tiempo real
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            currentScreen = "scanner"
        } else {
            Toast.makeText(context, "Permiso denegado: no se puede usar el escáner", Toast.LENGTH_SHORT).show()
        }
    }

    // GESTIÓN DE NAVEGACIÓN
    when (currentScreen) {
        "home" -> HomeScreen(
            viewModel = viewModel,
            onScanClick = {
                if (hasCameraPermission) currentScreen = "scanner"
                else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            },
            onViewClick = {
                idDetectado = null // Limpiamos ID para que sea registro manual
                currentScreen = "formulario"
            },
            onEditClick = {
                currentScreen = "lista_equipos" // Navega a la lista que acabas de crear
            }
        )

        "scanner" -> ScannerScreen(
            onCodeScanned = { codigo ->
                // Extrae el número final después del último guion
                val numeroString = codigo.substringAfterLast('-')
                idDetectado = numeroString.toIntOrNull()
                currentScreen = "formulario"
            },
            onBackPressed = { currentScreen = "home" }
        )

        "formulario" -> EquipoFormScreen(
            idDetectado = idDetectado,
            viewModel = viewModel,
            onBack = { currentScreen = "home" }
        )

        "lista_equipos" -> ListaEquiposScreen(
            viewModel = viewModel,
            onBack = { currentScreen = "home" }
        )
    }
}