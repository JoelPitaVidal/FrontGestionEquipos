package com.example.gestionequipos.data.remote

import com.example.gestionequipos.data.model.Equipo
import com.example.gestionequipos.data.model.Estacion
import com.example.gestionequipos.data.model.Usuario
import com.example.gestionequipos.data.model.TipoEquipo
import retrofit2.http.*

interface ApiService {

    // ==================== EQUIPOS ====================

    @GET("equipos/")
    suspend fun getEquipos(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): List<Equipo>

    @GET("equipos/{equipo_id}")
    suspend fun getEquipoPorId(
        @Path("equipo_id") equipoId: Int
    ): Equipo

    @POST("equipos/")
    suspend fun crearEquipo(
        @Body equipo: Equipo
    ): Equipo

    // ==================== USUARIOS ====================

    @GET("usuarios/")
    suspend fun getUsuarios(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): List<Usuario>

    @GET("usuarios/{usuario_id}")
    suspend fun getUsuarioPorId(
        @Path("usuario_id") usuarioId: Int
    ): Usuario


    // ==================== ESTACIONES ====================

    @GET("estaciones/")
    suspend fun getEstaciones(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): List<Estacion>

    @GET("estaciones/{estacion_id}")
    suspend fun getEstacionPorId(
        @Path("estacion_id") estacionId: Int
    ): Estacion

    @DELETE("estaciones/{estacion_id}")
    suspend fun eliminarEstacion(
        @Path("estacion_id") estacionId: Int
    ): retrofit2.Response<Unit> // Usamos Response<Unit> para el status 204 No Content
}