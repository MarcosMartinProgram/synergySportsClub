// Archivo: ValorActividadDAO.kt
// Ruta: app/src/main/java/com/martinmarcos/synergysportclub/data/dao/ValorActividadDAO.kt

package com.martinmarcos.synergysportclub.data.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.martinmarcos.synergysportclub.data.DBHelper

// ASEGÚRATE DE QUE EL CONSTRUCTOR RECIBE 'context: Context'
class ValorActividadDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    // ASEGÚRATE DE QUE ESTE MÉTODO EXISTE EXACTAMENTE ASÍ
    fun addValorActividad(idActividad: Int, monto: Double, fechaDesde: String): Long {
        val db = dbHelper.writableDatabase
        var nuevoId: Long = -1

        val values = ContentValues().apply {
            put("idActividad", idActividad)
            put("monto", monto)
            put("fechaDesde", fechaDesde)
        }

        try {
            // El nombre de la tabla debe coincidir con el de tu DBHelper.
            // En tu caso es "valor_actividad".
            nuevoId = db.insert("valor_actividad", null, values)
        } catch (e: Exception) {
            Log.e("ValorActividadDAO", "Error al insertar valor de actividad", e)
        }

        return nuevoId
    }
    @SuppressLint("Range")
    fun getMontoActualActividad(idActividad: Int): Double? {
        val db = dbHelper.readableDatabase
        var monto: Double? = null
        val query = "SELECT monto FROM valor_actividad WHERE idActividad = ? ORDER BY fechaDesde DESC LIMIT 1"

        db.rawQuery(query, arrayOf(idActividad.toString())).use { cursor ->
            if (cursor.moveToFirst()) {
                monto = cursor.getDouble(cursor.getColumnIndex("monto"))
                Log.i("ValorActividadDAO", "Monto encontrado para actividad $idActividad: $monto")
            } else {
                Log.w("ValorActividadDAO", "No se encontró precio para la actividad con ID: $idActividad")
            }
        }
        db.close()
        return monto
    }
}
