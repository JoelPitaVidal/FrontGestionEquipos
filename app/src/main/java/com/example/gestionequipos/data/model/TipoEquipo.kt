package com.example.gestionequipos.data.model

import com.google.gson.annotations.SerializedName

data class TipoEquipo(
    @SerializedName("tipequipo") val id: String,
    @SerializedName("tipequiposdescripcion") val descripcion: String?
)