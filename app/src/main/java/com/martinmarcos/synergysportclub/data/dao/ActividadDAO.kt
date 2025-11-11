package com.martinmarcos.synergysportclub.data.dao

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.martinmarcos.synergysportclub.data.DBHelper
import com.martinmarcos.synergysportclub.model.Actividad

class ActividadDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    fun addActividad(nomAct: String, horario: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("NomAct", nomAct)
            put("horario", horario)
        }
        var nuevoId: Long = -1
        try {
            nuevoId = db.insert("actividades", null, values)
        } catch (e: Exception) {
            Log.e("ActividadDAO", "Error al insertar actividad", e)
        } finally {
            db.close()
        }
        return nuevoId
    }

    fun getAllActividades(): List<Actividad> {
        val db = dbHelper.readableDatabase
        val cursor = db.query("actividades", null, null, null, null, null, "NomAct ASC")

        val actividades = mutableListOf<Actividad>()
        while (cursor.moveToNext()) {
            val actividad = Actividad(
                idActividad = cursor.getInt(cursor.getColumnIndexOrThrow("idActividad")),
                nomAct = cursor.getString(cursor.getColumnIndexOrThrow("NomAct")),
                horario = cursor.getString(cursor.getColumnIndexOrThrow("horario"))
            )
            actividades.add(actividad)
        }
        cursor.close()
        db.close()
        return actividades
    }
}