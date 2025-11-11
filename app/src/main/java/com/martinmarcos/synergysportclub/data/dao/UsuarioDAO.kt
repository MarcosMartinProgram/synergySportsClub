package com.martinmarcos.synergysportclub.data.dao

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.martinmarcos.synergysportclub.data.DBHelper
import com.martinmarcos.synergysportclub.model.Usuario

class UsuarioDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    fun addUsuario(mail: String, pass: String, idPersona: Long, rolUsu: Int): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("mail", mail)
            put("pass", pass) // Considera cifrar esta contraseña antes de guardarla
            put("idPersona", idPersona)
            put("RolUsu", rolUsu)
        }

        var nuevoId: Long = -1
        try {
            nuevoId = db.insert("usuario", null, values)
        } catch (e: Exception) {
            Log.e("UsuarioDAO", "Error al insertar usuario", e)
        } finally {
            db.close()
        }
        return nuevoId
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
}