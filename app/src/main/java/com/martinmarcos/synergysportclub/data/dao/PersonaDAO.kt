// ARCHIVO COMPLETO: data/dao/PersonaDAO.kt
package com.martinmarcos.synergysportclub.data.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.martinmarcos.synergysportclub.data.DBHelper
import com.martinmarcos.synergysportclub.model.Persona

class PersonaDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    // ================== FUNCIÓN CLAVE #1 ==================
    // ESTA FUNCIÓN DEBE ACEPTAR PARÁMETROS NULOS (con '?')
    fun addPersona(
        nombre: String,
        apellido: String,
        dni: String,
        idRol: Int,
        username: String?,
        pass: String?,
        fechaAlta: String,
        telefono: String?,
        email: String?,
        domicilio: String?,
        fechaNacimiento: String?,
        fichaMedica: Boolean
    ): Long {
        val db = dbHelper.writableDatabase
        var nuevoId: Long = -1L

        val values = ContentValues().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("dni", dni)
            put("idRol", idRol)
            put("username", username)
            put("password", pass)
            put("fechaAlta", fechaAlta)
            put("telefono", telefono)
            put("email", email)
            put("domicilio", domicilio)
            put("fechaNacimiento", fechaNacimiento)
            put("fichaMedica", if (fichaMedica) 1 else 0)
        }

        try {
            nuevoId = db.insertOrThrow("persona", null, values)
            Log.i("PersonaDAO", "Persona insertada con éxito. ID: $nuevoId, Rol: $idRol")
        } catch (e: Exception) {
            Log.e("PersonaDAO", "Error al insertar persona. DNI o Username podrían estar duplicados.", e)
            nuevoId = -1L
        }
        return nuevoId
    }
    @SuppressLint("Range")
    fun findSocioByDni(dni: String): Persona? {
        val db = dbHelper.readableDatabase
        var persona: Persona? = null

        // El ID del rol "Socio" en tu base de datos.
        // Basado en el orden de inserción en tu DBHelper, "Socio" es el segundo, por lo tanto, su ID es 2.
        val idRolSocio = 2

        val query = "SELECT * FROM persona WHERE dni = ? AND idRol = ?"
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(query, arrayOf(dni, idRolSocio.toString()))

            if (cursor.moveToFirst()) {
                // --- CONSTRUCCIÓN DEL OBJETO Persona A PRUEBA DE NULLS ---
                persona = Persona(
                    idPersona = cursor.getInt(cursor.getColumnIndex("idPersona")),

                    // CORRECCIÓN: Usamos '?: ""' para los campos String que no pueden ser nulos.
                    // Si la base de datos devuelve null, le asignamos una cadena vacía para evitar el crash.
                    nombre = cursor.getString(cursor.getColumnIndex("nombre")) ?: "",
                    apellido = cursor.getString(cursor.getColumnIndex("apellido")) ?: "",
                    dni = cursor.getString(cursor.getColumnIndex("dni")) ?: "",
                    idRol = cursor.getInt(cursor.getColumnIndex("idRol")),
                    username = cursor.getString(cursor.getColumnIndex("username")) ?: "",
                    fechaAlta = cursor.getString(cursor.getColumnIndex("fechaAlta")) ?: "",

                    activo = cursor.getInt(cursor.getColumnIndex("activo")) == 1,

                    // Para los campos que SÍ pueden ser nulos (String?), no necesitamos hacer nada especial.
                    // getString() devuelve null si el valor es NULL en la DB.
                    telefono = cursor.getString(cursor.getColumnIndex("telefono")),
                    email = cursor.getString(cursor.getColumnIndex("email")),
                    domicilio = cursor.getString(cursor.getColumnIndex("domicilio")),
                    fechaNacimiento = cursor.getString(cursor.getColumnIndex("fechaNacimiento")),

                    fichaMedica = cursor.getInt(cursor.getColumnIndex("fichaMedica")) == 1
                )
                Log.i("PersonaDAO", "Socio encontrado y objeto Persona completo creado: ${persona.nombre}")
            } else {
                Log.w("PersonaDAO", "No se encontró una persona con DNI '$dni' y rol de Socio.")
            }
        } catch (e: Exception) {
            Log.e("PersonaDAO", "Error al buscar y construir persona.", e)
        } finally {
            cursor?.close()
            db.close()
        }
        return persona
    }

    // El resto de tus funciones de PersonaDAO (validarUsuario, etc.)
    fun validarUsuario(username: String, pass: String): Int? {
        val db = dbHelper.readableDatabase
        var idRol: Int? = null
        val query = "SELECT idRol FROM persona WHERE username = ? AND password = ? AND activo = 1"
        try {
            db.rawQuery(query, arrayOf(username, pass)).use { cursor ->
                if (cursor.moveToFirst()) {
                    idRol = cursor.getInt(cursor.getColumnIndexOrThrow("idRol"))
                }
            }
        } catch (e: Exception) {
            Log.e("PersonaDAO", "Excepción al validar usuario.", e)
        }
        return idRol
    }

    fun getPersonaPorUsername(username: String): Persona? {
        val db = dbHelper.readableDatabase
        var persona: Persona? = null
        val query = "SELECT * FROM persona WHERE username = ?"
        try {
            db.rawQuery(query, arrayOf(username)).use { cursor ->
                if (cursor.moveToFirst()) {
                    persona = Persona(
                        idPersona = cursor.getInt(cursor.getColumnIndexOrThrow("idPersona")),
                        nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                        apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                        dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                        idRol = cursor.getInt(cursor.getColumnIndexOrThrow("idRol")),
                        username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                        activo = cursor.getInt(cursor.getColumnIndexOrThrow("activo")) == 1,
                        fechaAlta = cursor.getString(cursor.getColumnIndexOrThrow("fechaAlta")),
                        telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                        email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        domicilio = cursor.getString(cursor.getColumnIndexOrThrow("domicilio")),
                        fechaNacimiento = cursor.getString(cursor.getColumnIndexOrThrow("fechaNacimiento")),
                        fichaMedica = cursor.getInt(cursor.getColumnIndexOrThrow("fichaMedica")) == 1
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("PersonaDAO", "Error al buscar persona por username", e)
        }
        return persona
    }
    @SuppressLint("Range")
    fun findPersonaByDni(dni: String): Persona? {
        val db = dbHelper.readableDatabase
        var persona: Persona? = null

        // Consulta simple, solo por DNI
        val query = "SELECT * FROM persona WHERE dni = ?"
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(query, arrayOf(dni))

            if (cursor.moveToFirst()) {
                // Construimos el objeto Persona completo (versión a prueba de nulls)
                persona = Persona(
                    idPersona = cursor.getInt(cursor.getColumnIndex("idPersona")),
                    nombre = cursor.getString(cursor.getColumnIndex("nombre")) ?: "",
                    apellido = cursor.getString(cursor.getColumnIndex("apellido")) ?: "",
                    dni = cursor.getString(cursor.getColumnIndex("dni")) ?: "",
                    idRol = cursor.getInt(cursor.getColumnIndex("idRol")), // <-- Leemos el rol que tenga
                    username = cursor.getString(cursor.getColumnIndex("username")) ?: "",
                    activo = cursor.getInt(cursor.getColumnIndex("activo")) == 1,
                    fechaAlta = cursor.getString(cursor.getColumnIndex("fechaAlta")) ?: "",
                    telefono = cursor.getString(cursor.getColumnIndex("telefono")),
                    email = cursor.getString(cursor.getColumnIndex("email")),
                    domicilio = cursor.getString(cursor.getColumnIndex("domicilio")),
                    fechaNacimiento = cursor.getString(cursor.getColumnIndex("fechaNacimiento")),
                    fichaMedica = cursor.getInt(cursor.getColumnIndex("fichaMedica")) == 1
                )
                Log.i("PersonaDAO", "Persona encontrada con DNI $dni. Su rol es: ${persona.idRol}")
            } else {
                Log.w("PersonaDAO", "No se encontró ninguna persona con el DNI: $dni")
            }
        } catch (e: Exception) {
            Log.e("PersonaDAO", "Error al buscar persona por DNI.", e)
        } finally {
            cursor?.close()
            db.close()
        }
        return persona
    }
}
