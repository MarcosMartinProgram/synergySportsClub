// Archivo: ActividadDAO.kt
package com.martinmarcos.synergysportclub.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.martinmarcos.synergysportclub.data.DBHelper
import com.martinmarcos.synergysportclub.model.Actividad // <-- Importa el modelo

class ActividadDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    // ----------- CREATE -----------
    fun addActividad(nombre: String, horarios: String, dias: String, cupo: Int): Long {
        val db = dbHelper.writableDatabase
        var nuevoId: Long = -1
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("horarios", horarios)
            put("dias", dias)
            put("cupo", cupo)
        }
        try {
            nuevoId = db.insert("actividades", null, values)
        } catch (e: Exception) {
            Log.e("ActividadDAO", "Error al insertar actividad", e)
        }
        return nuevoId
    }

    // ----------- READ all -----------
    fun getAllActividades(): List<Actividad> {
        val actividades = mutableListOf<Actividad>()
        val db = dbHelper.readableDatabase
        try {
            db.query("actividades", null, null, null, null, null, "nombre ASC").use { cursor ->
                while (cursor.moveToNext()) {
                    actividades.add(cursorToActividad(cursor))
                }
            }
        } catch (e: Exception) {
            Log.e("ActividadDAO", "Error al obtener todas las actividades", e)
        }
        return actividades
    }

    // ----------- READ (uno) -----------
    fun getActividadById(id: Int): Actividad? {
        val db = dbHelper.readableDatabase
        var actividad: Actividad? = null
        try {
            db.query(
                "actividades",
                null,
                "idActividad = ?", // La cláusula WHERE
                arrayOf(id.toString()), // El valor para el WHERE
                null, null, null
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    actividad = cursorToActividad(cursor)
                }
            }
        } catch (e: Exception) {
            Log.e("ActividadDAO", "Error al obtener actividad por ID", e)
        }
        return actividad
    }

    // ----------- UPDATE -----------
    fun updateActividad(actividad: Actividad): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", actividad.nombre)
            put("horarios", actividad.horarios)
            put("dias", actividad.dias)
            put("cupo", actividad.cupo)
        }
        try {

            return db.update(
                "actividades",
                values,
                "idActividad = ?",
                arrayOf(actividad.idActividad.toString())
            )
        } catch (e: Exception) {
            Log.e("ActividadDAO", "Error al actualizar actividad", e)
            return 0 // 0 filas afectadas si hay error
        }
    }

    // ----------- DELETE -----------
    fun deleteActividad(id: Int): Int {
        val db = dbHelper.writableDatabase
        try {
            return db.delete(
                "actividades",
                "idActividad = ?",
                arrayOf(id.toString())
            )
        } catch (e: Exception) {
            Log.e("ActividadDAO", "Error al eliminar actividad", e)
            return 0 // 0 filas eliminadas si hay error
        }
    }

    private fun cursorToActividad(cursor: Cursor): Actividad {
        return Actividad(
            idActividad = cursor.getInt(cursor.getColumnIndexOrThrow("idActividad")),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
            horarios = cursor.getString(cursor.getColumnIndexOrThrow("horarios")),
            dias = cursor.getString(cursor.getColumnIndexOrThrow("dias")),
            cupo = cursor.getInt(cursor.getColumnIndexOrThrow("cupo"))
        )
    }
}
