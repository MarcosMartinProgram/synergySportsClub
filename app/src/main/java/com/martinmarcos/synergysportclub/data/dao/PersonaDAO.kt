// ARCHIVO COMPLETO: data/dao/PersonaDAO.kt
package com.martinmarcos.synergysportclub.data.dao

import android.content.ContentValues
import android.content.Context
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
}
