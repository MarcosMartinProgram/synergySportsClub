package com.martinmarcos.synergysportclub.model

class ValorActividad {


    data class ValorActividad(
        val idValorActividad: Int,
        val idActividad: Int,
        val monto: Double,
        val fechaDesde: String
    )
}