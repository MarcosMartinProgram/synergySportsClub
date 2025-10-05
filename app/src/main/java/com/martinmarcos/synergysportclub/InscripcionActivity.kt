package com.martinmarcos.synergysportclub

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class InscripcionActivity : AppCompatActivity() {

    private lateinit var btnAtras: ImageButton
    private lateinit var btnInscribir: Button
    private lateinit var inputFechaNacimiento: EditText

    private val calendario = Calendar.getInstance()
    private val formato = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscripcion)

        // Vistas
        btnAtras = findViewById(R.id.buttonAtras)
        btnInscribir = findViewById(R.id.btnInscribir)
        inputFechaNacimiento = findViewById(R.id.inputFechaNacimiento)

        // --- Botón ATRÁS -> MainActivity ---
        btnAtras.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish() //
        }

        // --- Botón INSCRIBIR -> otra pantalla ---
        btnInscribir.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
        }

        // --- DatePicker para Fecha de Nacimiento ---
        val listenerFecha = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendario.set(Calendar.YEAR, year)
            calendario.set(Calendar.MONTH, month)
            calendario.set(Calendar.DAY_OF_MONTH, day)
            inputFechaNacimiento.setText(formato.format(calendario.time))
        }

        // Tu XML ya tiene: focusable="false" y clickable="true" en el EditText, perfecto.
        inputFechaNacimiento.setOnClickListener {
            DatePickerDialog(
                this,
                listenerFecha,
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}
