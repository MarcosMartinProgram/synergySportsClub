package com.martinmarcos.synergysportclub.model

data class Actividad(
    val idActividad: Int,
    val nombre: String,
    val horarios: String,
    val dias: String,
    val cupo: Int
)
