package com.example.gestionequipos.data.repository

import android.util.Log
import com.example.gestionequipos.data.remote.ApiService
import com.example.gestionequipos.data.model.Equipo
import com.example.gestionequipos.data.model.Estacion
import com.example.gestionequipos.data.model.TipoEquipo
import com.example.gestionequipos.data.model.Usuario

class EquipoRepository(
    private val apiService: ApiService
) {

    // --- SECCIÓN EQUIPOS ---

    // Crear un equipo (Manual o QR)
    suspend fun crearEquipo(equipo: Equipo): Equipo {
        return apiService.crearEquipo(equipo)
    }

    // Leer lista completa desde SQL Server
    suspend fun leerEquipos(): List<Equipo> {
        Log.d("DEBUG_REPO", "Llamando a apiService.getEquipos()...")
        val respuesta = apiService.getEquipos()
        Log.d("DEBUG_REPO", "Respuesta recibida: ${respuesta.size} elementos")
        return respuesta
    }

    suspend fun leerTipos(): List<TipoEquipo> {
        return try {
            Log.d("DEBUG_REPO", "Solicitando tipos a la API...")
            val lista = apiService.getTipos()
            Log.d("DEBUG_REPO", "Tipos recibidos con éxito: ${lista.size}")
            lista
        } catch (e: Exception) {
            Log.e("DEBUG_REPO", "Error al leer tipos: ${e.message}")
            emptyList() // Si falla, devolvemos lista vacía para que no explote
        }
    }

    suspend fun leerEquipoPorId(equipoId: Int): Equipo {
        return apiService.getEquipoPorId(equipoId)
    }

    // --- SECCIÓN USUARIOS ---

    suspend fun leerUsuarios(): List<Usuario> {
        return apiService.getUsuarios()
    }

    // --- SECCIÓN ESTACIONES ---

    suspend fun leerEstaciones(): List<Estacion> {
        return apiService.getEstaciones()
    }

    // NOTA: He eliminado 'sincronizarEquipos' porque ahora guardamos
    // directamente en la API desde el formulario, eliminando errores de DAO.
}