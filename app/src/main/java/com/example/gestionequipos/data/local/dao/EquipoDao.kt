package com.example.gestionequipos.data.local.dao

// Importa las entidades para que Room sepa qué son EquipoEntity y EstadoEntity
import com.example.gestionequipos.data.local.Entity.EquipoEntity
import com.example.gestionequipos.data.local.Entity.EstadoEntity
import androidx.room.*

@Dao
interface EquipoDao {

    // --- OPERACIONES DE EQUIPOS ---

    /**
     * Inserta o actualiza un equipo.
     * Si el ID ya existe, sobreescribe los datos (ideal para actualizaciones offline).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipo(equipo: EquipoEntity)

    /**
     * Busca un equipo por su ID numérico real (EQUEQUIPO, ej: 223).
     */
    @Query("SELECT * FROM equipos WHERE id = :id")
    suspend fun obtenerEquipoPorId(id: Int): EquipoEntity?

    /**
     * Busca un equipo por su número de inventario con ceros (EQUNUMERO, ej: 0000223).
     * Útil si necesitas validar duplicados antes de guardar.
     */
    @Query("SELECT * FROM equipos WHERE numero = :num")
    suspend fun obtenerEquipoPorNumero(num: String): EquipoEntity?

    /**
     * Obtiene todos los equipos guardados localmente.
     */
    @Query("SELECT * FROM equipos")
    suspend fun obtenerTodosLosEquipos(): List<EquipoEntity>

    /**
     * Borra un equipo de la base de datos local.
     */
    @Delete
    suspend fun eliminarEquipo(equipo: EquipoEntity)


    // --- OPERACIONES PARA SINCRONIZACIÓN (OPCIONAL/FUTURO) ---

    /**
     * Si decides implementar el flag de sincronización que comentamos antes,
     * esta consulta te devolvería solo lo que falta por subir al servidor.
     */
    @Query("SELECT * FROM equipos WHERE sincronizado = 0")
    suspend fun obtenerEquiposPendientes(): List<EquipoEntity>


    // --- OPERACIONES DE MAESTROS (ESTADOS) ---

    /**
     * Inserta la lista de estados (ALTA/BAJA) para que estén disponibles offline.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEstados(estados: List<EstadoEntity>)

    /**
     * Obtiene los estados para llenar los selectores de la interfaz.
     */
    @Query("SELECT * FROM estados_alta_baja")
    suspend fun obtenerEstados(): List<EstadoEntity>


    @Query("SELECT * FROM equipos WHERE sincronizado = 0")
    suspend fun obtenerEquiposNoSincronizados(): List<EquipoEntity>

    @Query("UPDATE equipos SET sincronizado = 1 WHERE id = :id")
    suspend fun marcarComoSincronizado(id: Int)

}