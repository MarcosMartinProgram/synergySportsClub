package com.martinmarcos.synergysportclub.data.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.martinmarcos.synergysportclub.data.DBHelper
import com.martinmarcos.synergysportclub.model.ValorCuota // Asegúrate de que la ruta del modelo sea correcta
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ValorCuotaDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    /**
     * Inserta un nuevo registro de precio de cuota.
     * Siempre crea una nueva entrada para mantener un historial.
     */
    fun addNuevoValorCuota(descripcion: String, nuevoMonto: Double): Long {
        val db = dbHelper.writableDatabase
        var id: Long = -1

        // Obtenemos la fecha actual para 'fechaDesde'
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaActual = formatoFecha.format(Date())

        val values = ContentValues().apply {
            put("descripcion", descripcion)
            put("monto", nuevoMonto)
            put("fechaDesde", fechaActual)
        }

        try {
            id = db.insertOrThrow("valor_cuota", null, values)
            Log.i("ValorCuotaDAO", "Nuevo valor de cuota guardado con ID: $id y monto: $nuevoMonto")
        } catch (e: Exception) {
            Log.e("ValorCuotaDAO", "Error al insertar nuevo valor de cuota", e)
        } finally {
            db.close()
        }
        return id
    }

    /**
     * Obtiene el valor de cuota más reciente (el precio actual).
     * Lo busca ordenando por ID de forma descendente y tomando el primero.
     */
    @SuppressLint("Range")
    fun getValorCuotaActual(): ValorCuota? {
        val db = dbHelper.readableDatabase
        var valorCuota: ValorCuota? = null
        // Ordenamos por idValorCuota DESC y tomamos solo 1 resultado (el último)
        val cursor = db.query("valor_cuota", null, null, null, null, null, "idValorCuota DESC", "1")

        try {
            if (cursor.moveToFirst()) {
                val idValorCuota = cursor.getInt(cursor.getColumnIndex("idValorCuota"))
                val descripcion = cursor.getString(cursor.getColumnIndex("descripcion"))
                val monto = cursor.getDouble(cursor.getColumnIndex("monto"))
                val fechaDesde = cursor.getString(cursor.getColumnIndex("fechaDesde"))
                valorCuota = ValorCuota(idValorCuota, descripcion, monto, fechaDesde)
            }
        } catch (e: Exception) {
            Log.e("ValorCuotaDAO", "Error al obtener el valor de la cuota actual", e)
        } finally {
            cursor.close()
            db.close()
        }
        return valorCuota
    }
}