package com.martinmarcos.synergysportclub.model

data class Usuario(
    val idUsuario: Long,
    val mail: String,
    val pass: String,
    val idPersona: Long,
    val rolUsu: Int
)