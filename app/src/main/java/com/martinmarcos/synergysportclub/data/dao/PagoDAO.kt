package com.martinmarcos.synergysportclub.data.dao

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.martinmarcos.synergysportclub.data.DBHelper

class PagoDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    fun registrarPagoCuotaSocio(idSocio: Long, fechaPago: String, monto: Double, fechaVencimiento: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("idSocio", idSocio)
            put("fechaPago", fechaPago)
            put("monto", monto)
            put("fechaVencimiento", fechaVencimiento)
        }
        var nuevoId: Long = -1
        try {
            nuevoId = db.insert("pagosocio", null, values)
        } catch (e: Exception) {
            Log.e("PagoDAO", "Error al registrar pago de socio", e)
        } finally {
            db.close()
        }
        return nuevoId
    }

    // Aquí irían funciones como getPagosVencidos(fecha), getHistorialPagos(idSocio), etc.
}