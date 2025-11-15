package com.martinmarcos.synergysportclub.data.dao

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.martinmarcos.synergysportclub.data.DBHelper
import com.martinmarcos.synergysportclub.model.SocioConPersona

class SocioDAO(context: Context) {
    private val dbHelper = DBHelper(context)

    @SuppressLint("Range")
    fun findSocioByDni(dni: String): SocioConPersona? {
        val db = dbHelper.readableDatabase
        var socio: SocioConPersona? = null
        val query = """
            SELECT s.idSocio, p.idPersona, p.dni, p.nombre, p.apellido
            FROM socios s
            JOIN persona p ON s.idPersona = p.idPersona
            WHERE p.dni = ?
        """
        db.rawQuery(query, arrayOf(dni)).use { cursor -> // .use { } cierra el cursor automáticamente
            if (cursor.moveToFirst()) {
                socio = SocioConPersona(
                    idSocio = cursor.getInt(cursor.getColumnIndex("idSocio")),
                    idPersona = cursor.getInt(cursor.getColumnIndex("idPersona")),
                    dni = cursor.getString(cursor.getColumnIndex("dni")),
                    nombre = cursor.getString(cursor.getColumnIndex("nombre")),
                    apellido = cursor.getString(cursor.getColumnIndex("apellido"))
                )
                Log.i("SocioDAO", "Socio encontrado: ${socio.nombre} ${socio.apellido}")
            } else {
                Log.w("SocioDAO", "No se encontró ningún socio con el DNI: $dni")
            }
        }
        db.close()
        return socio
    }
}