package com.martinmarcos.synergysportclub.model

data class SocioConPersona(
    val idSocio: Int,
    val idPersona: Int,
    val dni: String,
    val nombre: String,
    val apellido: String
)