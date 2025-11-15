// Archivo: model/Persona.kt
package com.martinmarcos.synergysportclub.model

/**
 * Representa a una persona en el sistema, alineado con la tabla 'persona'
 * de la base de datos optimizada.
 */
data class Persona(
    // Estos son los nombres de parámetros que el DAO espera.
    // Deben coincidir exactamente.
    val idPersona: Int,
    val nombre: String,
    val apellido: String,
    val dni: String,
    val idRol: Int,
    val username: String,
    val activo: Boolean, // Usamos Boolean, es más limpio que Int
    val fechaAlta: String,
    val telefono: String?, // String? significa que puede ser nulo
    val email: String?,
    val domicilio: String?,
    val fechaNacimiento: String?,
    val fichaMedica: Boolean // Usamos Boolean, no String
)
