package com.martinmarcos.synergysportclub.data.dao

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.martinmarcos.synergysportclub.data.DBHelper
import java.text.SimpleDateFormat
import kotlin.text.format
import java.util.Date
import java.util.Locale

class PagoDAO(context: Context) {

    private val dbHelper = DBHelper(context)

    fun registrarPagoCuotaSocio(idPersona: Long, fechaPago: String, monto: Double, fechaVencimiento: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("idPersona", idPersona)
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
    fun registrarPagoGenerico(idPersona: Int, monto: Double, concepto: String): Long {
        val db = dbHelper.writableDatabase
        var id: Long = -1

        // Fecha actual en formato para la DB
        // --- CORRECCIÓN LÍNEA 37 ---
        // Usamos el 'Locale' estándar de Java
        val formatoDb = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // --- CORRECCIÓN LÍNEA 38 ---
        // Usamos el 'Date' estándar de Java para obtener la fecha y hora actuales
        val fechaActualDb = formatoDb.format(Date())

        val values = ContentValues().apply {
            put("idPersona", idPersona)
            put("fechaPago", fechaActualDb)
            put("montoPagado", monto)
            put("concepto", concepto) // Ej: "Pago de Actividad: Zumba"
        }

        try {
            id = db.insertOrThrow("pagos", null, values)
            Log.i("PagoDAO", "Pago genérico registrado con éxito para persona $idPersona. Concepto: $concepto")
        } catch (e: Exception) {
            Log.e("PagoDAO", "Error al registrar pago genérico.", e)
        } finally {
            db.close()
        }
        return id
    }

    // Aquí irían funciones como getPagosVencidos(fecha), getHistorialPagos(idSocio), etc.
}