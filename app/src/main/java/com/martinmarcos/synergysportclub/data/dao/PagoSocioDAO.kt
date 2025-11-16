package com.martinmarcos.synergysportclub.data.dao

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.martinmarcos.synergysportclub.data.DBHelper
import com.martinmarcos.synergysportclub.model.VencimientoSocio // Crearemos este modelo a continuación

class PagoSocioDAO(context: Context) {
    private val dbHelper = DBHelper(context)

    /**
     * Busca todos los pagos de socios que vencen en una fecha específica.
     * Une las tablas 'pagosocio' y 'persona' para obtener el nombre y DNI del socio.
     * @param fechaVencimiento La fecha en formato "yyyy-MM-dd".
     * @return Una lista de objetos VencimientoSocio.
     */
    @SuppressLint("Range")
    fun findVencimientosByDate(fechaVencimiento: String): List<VencimientoSocio> {
        val db = dbHelper.readableDatabase
        val vencimientos = mutableListOf<VencimientoSocio>()

        // Consulta SQL que une la tabla de pagos con la de personas
        val query = """
            SELECT p.nombre, p.apellido, p.dni, ps.monto, ps.fechaPago
            FROM pagosocio ps
            JOIN persona p ON ps.idPersona = p.idPersona
            WHERE ps.fechaVencimiento = ?
            ORDER BY p.apellido, p.nombre
        """

        try {
            db.rawQuery(query, arrayOf(fechaVencimiento)).use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val vencimiento = VencimientoSocio(
                            nombre = cursor.getString(cursor.getColumnIndex("nombre")),
                            apellido = cursor.getString(cursor.getColumnIndex("apellido")),
                            dni = cursor.getString(cursor.getColumnIndex("dni")),
                            monto = cursor.getDouble(cursor.getColumnIndex("monto")),
                            fechaPago = cursor.getString(cursor.getColumnIndex("fechaPago"))
                        )
                        vencimientos.add(vencimiento)
                    } while (cursor.moveToNext())
                    Log.i("PagoSocioDAO", "Se encontraron ${vencimientos.size} vencimientos para la fecha $fechaVencimiento")
                } else {
                    Log.i("PagoSocioDAO", "No se encontraron vencimientos para la fecha $fechaVencimiento")
                }
            }
        } catch (e: Exception) {
            Log.e("PagoSocioDAO", "Error al buscar vencimientos", e)
        } finally {
            db.close()
        }

        return vencimientos
    }

    @SuppressLint("Range")
    fun getUltimoVencimiento(idPersona: Int): String? {
        val db = dbHelper.readableDatabase
        var fechaVencimiento: String? = null
        val query = "SELECT fechaVencimiento FROM pagosocio WHERE idPersona = ? ORDER BY fechaVencimiento DESC LIMIT 1"
        db.rawQuery(query, arrayOf(idPersona.toString())).use { cursor ->
            if (cursor.moveToFirst()) {
                fechaVencimiento = cursor.getString(cursor.getColumnIndex("fechaVencimiento"))
            }
        }
        db.close()
        return fechaVencimiento
    }

}