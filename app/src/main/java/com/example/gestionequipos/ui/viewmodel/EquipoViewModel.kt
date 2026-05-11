package com.example.gestionequipos.ui.viewmodel

import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.gestionequipos.data.remote.RetrofitClient
import com.example.gestionequipos.data.repository.EquipoRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gestionequipos.data.model.Equipo
import kotlinx.coroutines.launch


class EquipoViewModel : ViewModel() {
    private val TAG = "PRUEBA_CONEXION"
    private val repository = EquipoRepository(RetrofitClient.instance as com.example.gestionequipos.data.remote.ApiService)
    private val _resultadoRegistro = MutableLiveData<Result<Equipo>>()
    val resultadoRegistro: LiveData<Result<Equipo>> = _resultadoRegistro

    private val _estaCargando = MutableLiveData<Boolean>()
    val estaCargando: LiveData<Boolean> = _estaCargando

    /**
     * Envía los datos del monitor/equipo escaneado a la API.
     * Usa el campo 'equequipo' como ID principal.
     */
    fun registrarEquipo(nuevoEquipo: Equipo) {
        viewModelScope.launch {
            _estaCargando.value = true
            try {
                val response = repository.crearEquipo(nuevoEquipo)
                _resultadoRegistro.value = Result.success(response)
                Log.d(TAG, "¡Éxito! Se guardó el equipo")
            } catch (e: Exception) {
                // Captura el error 400 si 'equequipo' ya existe
                _resultadoRegistro.value = Result.failure(e)
                Log.e(TAG, "Error fatal al conectar: ${e.message}")
            } finally {
                _estaCargando.value = false
            }
        }
    }
}