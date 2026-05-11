package com.example.gestionequipos.data.model

import com.google.gson.annotations.SerializedName

data class Equipo(
    @SerializedName("equequipo") val equequipo: Int, // ID numérico único
    @SerializedName("equdescripcion") val equdescripcion: String?,
    @SerializedName("equnumeroserie") val equnumeroserie: String?,
    @SerializedName("equtipo") val equtipo: Int?,
    @SerializedName("equestado") val equestado: String? = "A", // 'A' activo por defecto
    @SerializedName("equetiquetar") val equetiquetar: Boolean? = true,
    @SerializedName("equetiquetado") val equetiquetado: Boolean? = true
)