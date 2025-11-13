package com.martinmarcos.synergysportclub.model

data class Socio(
    val idSocio: Long,
    val idPersona: Long,
    val fechaAlta: String,
    val carnet: String,
    val precioCuota: Long
)