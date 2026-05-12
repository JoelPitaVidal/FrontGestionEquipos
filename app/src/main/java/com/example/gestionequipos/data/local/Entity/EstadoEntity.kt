package com.example.gestionequipos.data.local.Entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estados_alta_baja")
data class EstadoEntity(
    @PrimaryKey val estado: String,        // ESTALBAJESTADO ('A', 'B')
    val descripcion: String                // ESTALBAJDESCRIPCION ('ALTA', 'BAJA')
)