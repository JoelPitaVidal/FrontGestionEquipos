package com.example.gestionequipos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.gestionequipos.data.local.AppDatabase
import com.example.gestionequipos.data.local.Entity.EquipoEntity
import com.example.gestionequipos.ui.screens.HomeScreen
import com.example.gestionequipos.ui.screens.RegistroEquipoScreen
import com.example.gestionequipos.ui.screens.ScannerScreen
import com.example.gestionequipos.ui.viewmodel.EquipoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val dao = database.equipoDao()
        val viewModel = EquipoViewModel(dao)

        setContent {
            MainNavigation(viewModel)
        }
    }
}

@Composable
fun MainNavigation(viewModel: EquipoViewModel) {
    var currentScreen by remember { mutableStateOf("home") }
    val context = LocalContext.current

    // ✅ Estado para manejar permisos
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var permissionDeniedPermanently by remember { mutableStateOf(false) }

    // ✅ Launcher para solicitar permiso de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            // Permiso concedido, navegar al scanner
            currentScreen = "scanner"
        } else {
            // Permiso denegado
            permissionDeniedPermanently = true
            showPermissionDialog = true
        }
    }

    // Variables temporales para el QR
    var idDetectado by remember { mutableIntStateOf(0) }
    var numeroDetectado by remember { mutableStateOf("") }

    // ✅ Diálogo para cuando el permiso es denegado
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Permiso de Cámara Requerido") },
            text = {
                Text(
                    if (permissionDeniedPermanently) {
                        "La cámara es necesaria para escanear códigos QR. " +
                                "Por favor, habilita el permiso manualmente en Configuración > Aplicaciones > Gestión de Equipos > Permisos."
                    } else {
                        "Esta aplicación necesita acceso a la cámara para escanear códigos QR."
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionDialog = false
                    if (!permissionDeniedPermanently) {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPermissionDialog = false
                    currentScreen = "home"
                }) {
                    Text("Cancelar")
                }
            }
        )
    }

    when (currentScreen) {
        "home" -> HomeScreen(
            onScanClick = {
                // ✅ Verificar permiso antes de navegar
                if (hasCameraPermission) {
                    currentScreen = "scanner"
                } else {
                    // Solicitar permiso
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            onViewClick = {
                Toast.makeText(context, "Función no implementada", Toast.LENGTH_SHORT).show()
            },
            onEditClick = {
                Toast.makeText(context, "Función no implementada", Toast.LENGTH_SHORT).show()
            },
            viewModel = viewModel
        )

        "scanner" -> {
            // ✅ Doble verificación por seguridad
            if (hasCameraPermission) {
                ScannerScreen(
                    onCodeScanned = { codigo ->
                        // Procesar EQ-A00-0000223 -> numero: 0000223, id: 223
                        val numero = codigo.substringAfterLast('-')
                        val id = numero.toIntOrNull() ?: 0

                        idDetectado = id
                        numeroDetectado = numero
                        currentScreen = "formulario"
                    },
                    onBackPressed = {
                        currentScreen = "home"
                    }
                )
            } else {
                // Si de alguna forma llegamos aquí sin permiso, volver al home
                LaunchedEffect(Unit) {
                    currentScreen = "home"
                    Toast.makeText(context, "Permiso de cámara no concedido", Toast.LENGTH_SHORT).show()
                }
            }
        }

        "formulario" -> RegistroEquipoScreen(
            idEscaneado = idDetectado,
            numeroEscaneado = numeroDetectado,
            onGuardarClick = { equipo: EquipoEntity ->
                viewModel.guardarEquipoLocal(equipo) {
                    currentScreen = "home"
                    Toast.makeText(context, "Guardado en Local", Toast.LENGTH_SHORT).show()
                }
            },
            onCancelar = { currentScreen = "home" }
        )
    }
}