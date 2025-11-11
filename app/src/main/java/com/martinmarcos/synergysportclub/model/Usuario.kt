package com.martinmarcos.synergysportclub.model

data class Usuario(
    val idUsuario: Long,
    val username: String,
    val mail: String,
    val pass: String,
    val idPersona: Long,
    val rolUsu: Int
)