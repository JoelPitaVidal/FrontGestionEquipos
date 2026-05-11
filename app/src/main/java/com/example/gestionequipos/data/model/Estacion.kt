package com.example.gestionequipos.data.model

import com.google.gson.annotations.SerializedName

data class Estacion(
    @SerializedName("estestacion") val id: Int,
    @SerializedName("estnombre") val nombre: String?,
    @SerializedName("estusuario") val usuarioId: Int?,
    @SerializedName("estactiva") val activa: Boolean?,
    @SerializedName("estenuso") val enUso: Boolean?,
    @SerializedName("estextension") val extension: Int?,
    @SerializedName("estdepartamento") val departamento: String?
)