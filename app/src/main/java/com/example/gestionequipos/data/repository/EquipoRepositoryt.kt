package com.example.gestionequipos.data.repository

// BORRA CUALQUIER LINEA QUE DIGA FIREBASE
import android.util.Log
import com.example.gestionequipos.data.local.dao.EquipoDao
import com.example.gestionequipos.data.remote.ApiService
import com.example.gestionequipos.data.model.Equipo
import com.example.gestionequipos.data.model.Estacion
import com.example.gestionequipos.data.model.Usuario

// Asegúrate de que NO tenga la ruta de Firebase aquí abajo
class EquipoRepository(
    private val apiService: ApiService,
    private val equipoDao: EquipoDao // Tu DAO de Room
) {

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

    suspend fun sincronizarEquipos(): Result<Int> {
        return try {
            // 1. Buscamos en Room los equipos que NO se han enviado (sincronizado = 0)
            val pendientes = equipoDao.obtenerEquiposNoSincronizados()
            var contador = 0

            pendientes.forEach { local ->
                // 2. IMPORTANTE: Aquí pasamos los datos del QR a la API
                val paraEnviar = Equipo(
                    equequipo = local.id,           // El ID que vino del QR
                    equdescripcion = local.descripcion,
                    equnumeroserie = local.serie,
                    equtipo = local.tipo,
                    equestado = local.estado,
                    equetiquetar = local.etiquetar,
                    equetiquetado = true
                )

                // 3. Enviamos a Python
                apiService.crearEquipo(paraEnviar)

                // 4. Marcamos como enviado en Room para no repetirlo
                equipoDao.marcarComoSincronizado(local.id)
                contador++
            }
            Result.success(contador)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}