package com.martinmarcos.synergysportclub.model

data class PagoSocio(
    val idPago: Int,
    val idPersona: Int,
    val fechaPago: String,
    val monto: Double,
    val fechaVencimiento: String
)