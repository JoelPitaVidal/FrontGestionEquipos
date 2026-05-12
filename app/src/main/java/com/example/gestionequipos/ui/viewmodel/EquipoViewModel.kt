package com.example.gestionequipos.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionequipos.data.local.Entity.EquipoEntity
import com.example.gestionequipos.data.local.dao.EquipoDao
import com.example.gestionequipos.data.model.Equipo
import com.example.gestionequipos.data.remote.RetrofitClient
import com.example.gestionequipos.data.repository.EquipoRepository
import kotlinx.coroutines.launch

class EquipoViewModel(private val equipoDao: EquipoDao) : ViewModel() {

    private val TAG = "EQUIPO_VIEWMODEL"

    // Estado para la carga de sincronización (Compose)
    var cargandoSync by mutableStateOf(false)
        private set

    // Inicializamos el repositorio correctamente pasando el DAO recibido y el cliente API
    private val repository = EquipoRepository(
        equipoDao = equipoDao,
        apiService = RetrofitClient.instance
    )

    // LiveData para resultados de registro individual
    private val _resultadoRegistro = MutableLiveData<Result<Equipo>>()
    val resultadoRegistro: LiveData<Result<Equipo>> = _resultadoRegistro

    // LiveData para el estado de carga general (UI tradicional)
    private val _estaCargando = MutableLiveData<Boolean>()
    val estaCargando: LiveData<Boolean> = _estaCargando

    /**
     * Envía los datos del equipo escaneado a la API.
     */
    fun registrarEquipo(nuevoEquipo: Equipo) {
        viewModelScope.launch {
            _estaCargando.value = true
            try {
                val response = repository.crearEquipo(nuevoEquipo)
                _resultadoRegistro.value = Result.success(response)
                Log.d(TAG, "¡Éxito! Equipo guardado en la API")
            } catch (e: Exception) {
                _resultadoRegistro.value = Result.failure(e)
                Log.e(TAG, "Error al registrar: ${e.message}")
            } finally {
                _estaCargando.value = false
            }
        }
    }

    /**
     * Guarda el equipo en la base de datos local (Room).
     */
    fun guardarEquipoLocal(equipo: EquipoEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                equipoDao.insertEquipo(equipo)
                onSuccess()
            } catch (e: Exception) {
                Log.e(TAG, "Error al guardar local: ${e.message}")
            }
        }
    }

    /**
     * Inicia el proceso de envío de datos locales pendientes a la API.
     */
    fun iniciarSincronizacion(onSuccess: (Int) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            cargandoSync = true
            try {
                val resultado = repository.sincronizarEquipos()

                resultado.onSuccess { count ->
                    onSuccess(count)
                }.onFailure { error ->
                    onError(error.message ?: "Error desconocido en la sincronización")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Error fatal de conexión")
            } finally {
                cargandoSync = false
            }
        }
    }
}