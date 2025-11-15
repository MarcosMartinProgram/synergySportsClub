package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.martinmarcos.synergysportclub.R
import com.martinmarcos.synergysportclub.data.dao.PersonaDAO
import com.martinmarcos.synergysportclub.data.dao.SocioDAO

class CobrarCuotaActivity : AppCompatActivity() {

    private lateinit var etDniSocio: EditText
    private lateinit var btnBuscarSocio: Button
    private lateinit var personaDAO: PersonaDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobrar_cuota)

        etDniSocio = findViewById(R.id.editTextDniSocio)
        // OJO: Renombra el botón en tu XML a algo más claro si sigue llamándose 'buttonRecuperar'
        btnBuscarSocio = findViewById(R.id.buttonRecuperar)
        personaDAO = PersonaDAO(this)

        btnBuscarSocio.setOnClickListener {
            buscarYProcederAlCobro()
        }
    }

    private fun buscarYProcederAlCobro() {
        val dni = etDniSocio.text.toString().trim()
        if (dni.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un DNI", Toast.LENGTH_SHORT).show()
            return
        }

        val personaEncontrada = personaDAO.findSocioByDni(dni)

        if (personaEncontrada != null) {
            // ¡Persona encontrada y es un Socio! Procedemos.
            val intent = Intent(this, CobrarCuotaDatosActivity::class.java)

            // Pasamos los datos a la siguiente pantalla.
            // OJO: La siguiente pantalla esperará "ID_PERSONA" en lugar de "ID_SOCIO"
            intent.putExtra("ID_PERSONA", personaEncontrada.idPersona)
            intent.putExtra("DNI_PERSONA", personaEncontrada.dni)
            intent.putExtra("NOMBRE_PERSONA", "${personaEncontrada.nombre} ${personaEncontrada.apellido}")

            startActivity(intent)
        } else {
            Toast.makeText(this, "No se encontró ningún socio con el DNI ingresado.", Toast.LENGTH_LONG).show()
        }
    }
}