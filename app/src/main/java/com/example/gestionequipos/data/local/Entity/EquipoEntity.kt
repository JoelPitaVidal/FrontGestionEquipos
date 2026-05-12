package com.example.gestionequipos.data.local.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipos")
data class EquipoEntity(
    @PrimaryKey val id: Int,               // EQUEQUIPO (Obligatorio)
    val numero: String,                    // EQUNUMERO (Obligatorio)
    val descripcion: String,               // EQUDESCRIPCION (Obligatorio)
    val serie: String,                     // EQUNUMEROSERIE (Obligatorio)
    val estado: String,                    // EQUESTADO (Obligatorio)
    val tipo: Int,                         // EQUTIPO (Obligatorio)
    val etiquetar: Boolean = true,         // EQUETIQUETAR (Siempre True)

    // Campo extra para saber qué registros no se han enviado aún
    val sincronizado: Boolean = false
)
