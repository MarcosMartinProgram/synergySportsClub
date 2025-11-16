package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.martinmarcos.synergysportclub.R
import com.martinmarcos.synergysportclub.data.dao.PagoSocioDAO
import com.martinmarcos.synergysportclub.data.dao.PersonaDAO
import java.text.SimpleDateFormat
import java.util.Locale
import android.widget.ImageButton

class GenerarCarnetActivity : AppCompatActivity() {

    private lateinit var etDni: EditText
    private lateinit var btnGenerar: Button
    private lateinit var personaDAO: PersonaDAO
    private lateinit var pagoSocioDAO: PagoSocioDAO
    private lateinit var btnAtras: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generar_carnet)

        etDni = findViewById(R.id.editTextDniCarnet)
        btnGenerar = findViewById(R.id.buttonGenerarCarnet)
        btnAtras = findViewById(R.id.buttonAtras)

        personaDAO = PersonaDAO(this)
        pagoSocioDAO = PagoSocioDAO(this)

        btnGenerar.setOnClickListener {
            buscarSocioYGenerar()
        }
        btnAtras.setOnClickListener {
            finish()
        }
    }

    private fun buscarSocioYGenerar() {
        val dni = etDni.text.toString().trim()
        if (dni.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un DNI.", Toast.LENGTH_SHORT).show()
            return
        }

        // 1. Buscamos solo socios (idRol = 2)
        val socio = personaDAO.findSocioByDni(dni)

        if (socio == null) {
            Toast.makeText(this, "Socio no encontrado o la persona no es un socio.", Toast.LENGTH_LONG).show()
            return
        }

        // 2. (Opcional pero recomendado) Buscamos el último pago para obtener la fecha de vencimiento
        val ultimoVencimiento = pagoSocioDAO.getUltimoVencimiento(socio.idPersona)
        var fechaVencimientoStr = "Sin pagos"
        if (ultimoVencimiento != null) {
            // Convertimos la fecha de 'yyyy-MM-dd' a 'dd/MM/yyyy' para mostrar
            try {
                val formatoDB = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formatoUI = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fecha = formatoDB.parse(ultimoVencimiento)
                if (fecha != null) {
                    fechaVencimientoStr = formatoUI.format(fecha)
                }
            } catch (e: Exception) {
                fechaVencimientoStr = ultimoVencimiento // Si falla, mostramos como está
            }
        }

        // 3. Lanzamos la Activity que mostrará el carnet
        val intent = Intent(this, CarnetDisplayActivity::class.java).apply {
            putExtra("NOMBRE", socio.nombre)
            putExtra("APELLIDO", socio.apellido)
            putExtra("DNI", socio.dni)
            putExtra("ID_SOCIO", socio.idPersona)
            putExtra("FECHA_VENCIMIENTO", fechaVencimientoStr)
        }
        startActivity(intent)
    }
}
