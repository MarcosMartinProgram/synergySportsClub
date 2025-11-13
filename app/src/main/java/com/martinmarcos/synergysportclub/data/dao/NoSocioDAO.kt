package com.martinmarcos.synergysportclub.data.dao

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.martinmarcos.synergysportclub.data.DBHelper
import com.martinmarcos.synergysportclub.model.NoSocio

class NoSocioDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    fun addNoSocio(idPersona: Long, fechaActividad: String, habilitado: String, idActividad: Long): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("idPersona", idPersona)
            put("fechaActividad", fechaActividad)
            put("habilitado", habilitado)
            put("idActividad", idActividad)
        }

        var nuevoId: Long = -1
        try {
            nuevoId = db.insert("nosocio", null, values)
        } catch (e: Exception) {
            Log.e("NoSocioDAO", "Error al insertar No Socio", e)
        } finally {
            db.close()
        }
        return nuevoId
    }

/*
    fun getUsuarioPorUsername(username: String): Usuario? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "usuario",
            null, // todas las columnas
            "username = ?", // la condición de búsqueda
            arrayOf(username), // el valor para la condición
            null, null, null
        )

        var usuario: Usuario? = null
        if (cursor.moveToFirst()) {
            usuario = Usuario(
                idUsuario = cursor.getLong(cursor.getColumnIndexOrThrow("idUsuario")),
                username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                mail = cursor.getString(cursor.getColumnIndexOrThrow("mail")),
                pass = cursor.getString(cursor.getColumnIndexOrThrow("pass")),
                idPersona = cursor.getLong(cursor.getColumnIndexOrThrow("idPersona")),
                rolUsu = cursor.getInt(cursor.getColumnIndexOrThrow("RolUsu"))
            )
        }
        cursor.close()
        db.close()
        return usuario
    }

    fun getUsuarioPorMail(mail: String): Usuario? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "usuario",
            null,
            "mail = ?",
            arrayOf(mail),
            null, null, null
        )

        var usuario: Usuario? = null
        if (cursor.moveToFirst()) {
            usuario = Usuario(
                idUsuario = cursor.getLong(cursor.getColumnIndexOrThrow("idUsuario")),
                username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                mail = cursor.getString(cursor.getColumnIndexOrThrow("mail")),
                pass = cursor.getString(cursor.getColumnIndexOrThrow("pass")),
                idPersona = cursor.getLong(cursor.getColumnIndexOrThrow("idPersona")),
                rolUsu = cursor.getInt(cursor.getColumnIndexOrThrow("RolUsu"))
            )
        }
        cursor.close()
        db.close()
        return usuario
    }

 */
}