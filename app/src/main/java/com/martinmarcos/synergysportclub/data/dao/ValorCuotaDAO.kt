package com.martinmarcos.synergysportclub.data.dao

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.martinmarcos.synergysportclub.data.DBHelper

class ValorCuotaDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    fun addValorCuota(monto: Double, fechaDesde: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("monto", monto)
            put("fechaDesde", fechaDesde)
        }
        return try {
            db.insert("valor_cuota", null, values)
        } catch (e: Exception) {
            Log.e("ValorCuotaDAO", "Error al insertar valor de cuota", e)
            -1L // Devuelve -1 en caso de error
        }
    }
}
