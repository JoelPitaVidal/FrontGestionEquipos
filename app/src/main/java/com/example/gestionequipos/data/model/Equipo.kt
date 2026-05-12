package com.example.gestionequipos.data.model

import com.google.gson.annotations.SerializedName

data class Equipo(
    @SerializedName("equequipo") val equequipo: Int? = null,
    @SerializedName("equnumero") val equnumero: String? = null,
    @SerializedName("equdescripcion") val equdescripcion: String? = "",
    @SerializedName("equnumeroserie") val equnumeroserie: String? = "",
    @SerializedName("equestado") val equestado: String? = "A",
    @SerializedName("equtipo") val equtipo: Int? = 0,
    // CAMBIO AQUÍ: de Int a Boolean?
    @SerializedName("equetiquetar") val equetiquetar: Boolean? = false
)