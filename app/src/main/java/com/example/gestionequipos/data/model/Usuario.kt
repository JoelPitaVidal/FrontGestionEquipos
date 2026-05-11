package com.example.gestionequipos.data.model

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("usrsusuario") val id: Int,
    @SerializedName("usrnombre") val nombre: String?
)