package com.martinmarcos.synergysportclub.data.dao

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.martinmarcos.synergysportclub.data.DBHelper
import com.martinmarcos.synergysportclub.model.Persona

class PersonaDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    fun addPersona(
        nombre: String,
        apellido: String,
        dni: String,
        fechaNacimiento: String?,
        domicilio: String?,
        telefono: String?,
        fichaMedica: String?
    ): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("dni", dni)
            put("fechaNacimiento", fechaNacimiento)
            put("domicilio", domicilio)
            put("telefono", telefono)
            put("fichaMedica", fichaMedica)
        }

        var nuevoId: Long = -1
        try {
            nuevoId = db.insert("persona", null, values)
        } catch (e: Exception) {
            Log.e("PersonaDAO", "Error al insertar persona", e)
        } finally {
            db.close()
        }
        return nuevoId
    }

    fun getPersonaPorDni(dni: String): Persona? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "persona",
            null, // todas las columnas
            "dni = ?",
            arrayOf(dni),
            null,
            null,
            null
        )

        var persona: Persona? = null
        if (cursor.moveToFirst()) {
            persona = Persona(
                id = cursor.getLong(cursor.getColumnIndexOrThrow("idPersona")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                fechaNacimiento = cursor.getString(cursor.getColumnIndexOrThrow("fechaNacimiento")),
                domicilio = cursor.getString(cursor.getColumnIndexOrThrow("domicilio")),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                fichaMedica = cursor.getString(cursor.getColumnIndexOrThrow("fichaMedica"))
            )
        }
        cursor.close()
        db.close()
        return persona
    }
}