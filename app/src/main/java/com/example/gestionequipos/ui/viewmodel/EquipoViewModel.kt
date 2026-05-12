package com.example.gestionequipos.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionequipos.data.model.Equipo
import com.example.gestionequipos.data.repository.EquipoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EquipoViewModel(private val repository: EquipoRepository) : ViewModel() {

    private val TAG = "EQUIPO_VIEWMODEL"

    private val _equipos = MutableStateFlow<List<Equipo>>(emptyList())
    val equipos: StateFlow<List<Equipo>> = _equipos

    // Nuevo StateFlow para guardar el mapa de nombres de tipos
    // Ejemplo: Map<16, "PORTATIL">
    private val _nombresTipos = MutableStateFlow<Map<Int, String>>(emptyMap())
    val nombresTipos: StateFlow<Map<Int, String>> = _nombresTipos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Carga de datos completa: Primero tipos, luego equipos.
     */
    fun cargarDatosInventario() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Cargamos los tipos primero
                val tiposDescargados = repository.leerTipos()

                // 2. Creamos el mapa (ID -> Nombre)
                _nombresTipos.value = tiposDescargados.associate { it.idtipo to it.tipodescripcion }
                Log.d("DEBUG_VM", "Mapa de nombres creado: ${_nombresTipos.value}")

                // 3. Cargamos los equipos
                val listaEquipos = repository.leerEquipos()
                _equipos.value = listaEquipos

            } catch (e: Exception) {
                Log.e("DEBUG_VM", "Error en carga: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Mantengo esta por compatibilidad o para el botón "Actualizar"
    fun cargarEquipos() = cargarDatosInventario()

    fun crearEquipo(nuevoEquipo: Equipo, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.crearEquipo(nuevoEquipo)
                Log.d(TAG, "¡Éxito! Equipo guardado")
                cargarDatosInventario() // Recargamos todo
                onResult(true)
            } catch (e: Exception) {
                Log.e(TAG, "Error al registrar: ${e.message}")
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }
}