package com.martinmarcos.synergysportclub.data.dao

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.martinmarcos.synergysportclub.data.DBHelper
import com.martinmarcos.synergysportclub.model.Socio

class SocioDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    fun addSocio(idPersona: Long, fechaAlta: String, carnet: String, precioCuota: Long): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("idPersona", idPersona)
            put("fechaAlta", fechaAlta)
            put("carnet", carnet)
            put("precioCuota", precioCuota)
        }

        var nuevoId: Long = -1
        try {
            nuevoId = db.insert("socio", null, values)
        } catch (e: Exception) {
            Log.e("SocioDAO", "Error al insertar Socio", e)
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