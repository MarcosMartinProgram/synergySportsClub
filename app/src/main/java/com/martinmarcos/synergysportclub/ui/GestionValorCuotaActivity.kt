package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.martinmarcos.synergysportclub.R
import com.martinmarcos.synergysportclub.data.dao.ValorCuotaDAO
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GestionValorCuotaActivity : AppCompatActivity() {

    // DAO
    private lateinit var valorCuotaDAO: ValorCuotaDAO

    // Vistas del Layout
    private lateinit var etMontoCuota: EditText
    private lateinit var etFechaDesdeCuota: EditText
    private lateinit var btnGrabarValorCuota: Button
    private lateinit var btnSalir: Button
    private lateinit var tvMensajeExitoCuota: TextView
    private lateinit var buttonAtrasMenu: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_valor_cuota)

        // 1. Inicializar DAO y Vistas
        valorCuotaDAO = ValorCuotaDAO(this)
        etMontoCuota = findViewById(R.id.etMontoCuota)
        etFechaDesdeCuota = findViewById(R.id.etFechaDesdeCuota)
        btnGrabarValorCuota = findViewById(R.id.btnGrabarValorCuota)
        tvMensajeExitoCuota = findViewById(R.id.tvMensajeExitoCuota)
        btnSalir = findViewById(R.id.btnSalir)
        buttonAtrasMenu = findViewById(R.id.buttonAtras)

        // 2. Configurar el botón de Grabar
        btnGrabarValorCuota.setOnClickListener {
            guardarNuevoValorCuota()
        }

        // 3. Ocultar mensaje de éxito si el usuario empieza a editar
        etMontoCuota.doAfterTextChanged {
            tvMensajeExitoCuota.visibility = View.INVISIBLE
        }

        // 4. Configurar botones de navegación
        btnSalir.setOnClickListener {
            finish() // Cierra la actividad actual y vuelve a la anterior
        }

        buttonAtrasMenu.setOnClickListener {
            finish() // Lo mismo para el botón de la flecha
        }
    }

    private fun guardarNuevoValorCuota() {
        // Obtener y validar el monto
        val montoStr = etMontoCuota.text.toString().trim()
        if (montoStr.isBlank()) {
            etMontoCuota.error = "El monto no puede estar vacío"
            return
        }
        val monto = montoStr.toDoubleOrNull()
        if (monto == null || monto <= 0) {
            etMontoCuota.error = "Ingrese un valor numérico positivo"
            return
        }

        // Obtener la fecha (si está vacía, usar la de hoy)
        var fecha = etFechaDesdeCuota.text.toString().trim()
        if (fecha.isBlank()) {
            fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        }

        // Guardar en la base de datos usando el DAO
        val idGenerado = valorCuotaDAO.addValorCuota(monto, fecha)

        if (idGenerado != -1L) {
            // Mostrar mensaje de éxito en pantalla
            tvMensajeExitoCuota.visibility = View.VISIBLE
            limpiarFormulario()
        } else {
            Toast.makeText(this, "Error al guardar el valor de la cuota", Toast.LENGTH_LONG).show()
        }
    }

    private fun limpiarFormulario() {
        etMontoCuota.text.clear()
        etFechaDesdeCuota.text.clear()
        // Pone el foco de nuevo en el primer campo para una entrada rápida
        etMontoCuota.requestFocus()
    }
}
