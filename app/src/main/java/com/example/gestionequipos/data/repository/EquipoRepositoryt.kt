package com.example.gestionequipos.data.repository

// BORRA CUALQUIER LINEA QUE DIGA FIREBASE
import com.example.gestionequipos.data.remote.ApiService
import com.example.gestionequipos.data.model.Equipo
import com.example.gestionequipos.data.model.Estacion
import com.example.gestionequipos.data.model.Usuario

// Asegúrate de que NO tenga la ruta de Firebase aquí abajo
class EquipoRepository(private val apiService: ApiService) {

    suspend fun crearEquipo(equipo: Equipo): Equipo {
        return apiService.crearEquipo(equipo)
    }

    // EQUIPOS (Basado en equipos.py)
    suspend fun leerEquipos(skip: Int = 0, limit: Int = 100): List<Equipo> {
        return apiService.getEquipos(skip, limit)
    }

    suspend fun leerEquipoPorId(equipoId: Int): Equipo {
        return apiService.getEquipoPorId(equipoId)
    }

    // USUARIOS (Basado en usuarios.py)
    suspend fun leerUsuarios(skip: Int = 0, limit: Int = 100): List<Usuario> {
        return apiService.getUsuarios(skip, limit)
    }

    // ESTACIONES (Basado en estaciones.py)
    suspend fun leerEstaciones(skip: Int = 0, limit: Int = 100): List<Estacion> {
        return apiService.getEstaciones(skip, limit)
    }
}