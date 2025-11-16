package com.martinmarcos.synergysportclub.model

data class VencimientoSocio(
    val nombre: String,
    val apellido: String,
    val dni: String,
    val monto: Double,
    val fechaPago: String
)