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

    private lateinit var etPrecioCuota: EditText
    private lateinit var btnGuardarPrecio: Button
    private lateinit var valorCuotaDAO: ValorCuotaDAO
    private lateinit var btnAtras: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_valor_cuota)

        // Inicializar vistas y DAO
        etPrecioCuota = findViewById(R.id.etMontoCuota)
        btnGuardarPrecio = findViewById(R.id.btnGrabarValorCuota)
        valorCuotaDAO = ValorCuotaDAO(this)

        // Cargar el precio actual al abrir la pantalla
        cargarPrecioActual()

        btnGuardarPrecio.setOnClickListener {
            guardarNuevoPrecio()
        }
        btnAtras = findViewById(R.id.buttonAtras)
        btnAtras.setOnClickListener {

            finish()
        }
    }

    private fun cargarPrecioActual() {
        // Usamos el nuevo método para obtener el último precio guardado
        val cuotaActual = valorCuotaDAO.getValorCuotaActual()
        if (cuotaActual != null) {
            etPrecioCuota.setText(cuotaActual.monto.toString())
        } else {
            etPrecioCuota.setText("0.0") // Valor por defecto si la tabla está vacía
        }
    }

    private fun guardarNuevoPrecio() {
        val precioTexto = etPrecioCuota.text.toString().trim()

        if (precioTexto.isEmpty()) {
            Toast.makeText(this, "El precio no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevoMonto = precioTexto.toDoubleOrNull()
        if (nuevoMonto == null || nuevoMonto < 0) {
            Toast.makeText(this, "Por favor, ingrese un precio válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Usamos el nuevo método que siempre inserta un registro
        val id = valorCuotaDAO.addNuevoValorCuota("Cuota Mensual General", nuevoMonto)

        if (id != -1L) {
            Toast.makeText(this, "¡Nuevo precio de cuota guardado con éxito!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Error al guardar el precio.", Toast.LENGTH_SHORT).show()
        }
    }
}
