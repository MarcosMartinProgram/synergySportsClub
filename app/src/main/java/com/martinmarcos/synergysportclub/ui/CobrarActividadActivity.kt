package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText // <-- Importante: Añadir EditText
import android.widget.ImageButton
import android.widget.Toast // <-- Importante: Añadir Toast para mensajes
import androidx.appcompat.app.AppCompatActivity
import com.martinmarcos.synergysportclub.R
import com.martinmarcos.synergysportclub.data.dao.PersonaDAO // <-- Importante: Añadir el DAO

class CobrarActividadActivity : AppCompatActivity() {

    // 1. Declarar las vistas y el DAO
    private lateinit var etDniPersona: EditText
    private lateinit var btnBuscarPersona: Button
    private lateinit var personaDAO: PersonaDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // La línea enableEdgeToEdge() a menudo se elimina si no se usa un tema específico.
        // Si te da problemas o no la necesitas, puedes borrarla.
        // enableEdgeToEdge()
        setContentView(R.layout.activity_cobrar_actividad)

        // 2. Inicializar las vistas y el DAO
        // Asegúrate de que los IDs en tu XML coincidan (ej: 'editTextDniActividad' y 'buttonBuscarNoSocio')
        etDniPersona = findViewById(R.id.editTextDniPersona)
        btnBuscarPersona = findViewById(R.id.buttonBuscarNoSocio)
        personaDAO = PersonaDAO(this)

        // 3. Configurar el listener del botón de búsqueda
        btnBuscarPersona.setOnClickListener {
            buscarPersonaYProceder()
        }

        val buttonAtrasMenu = findViewById<ImageButton>(R.id.buttonAtras)
        buttonAtrasMenu.setOnClickListener {
            // Esta parte está bien, vuelve al menú principal
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }
    }

    // 4. Implementar la función de búsqueda
    private fun buscarPersonaYProceder() {
        val dni = etDniPersona.text.toString().trim()
        if (dni.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un DNI", Toast.LENGTH_SHORT).show()
            return
        }

        // --- ¡AQUÍ ESTÁ LA LÓGICA NUEVA! ---
        // Usamos la función genérica que busca a cualquier persona por su DNI
        val personaEncontrada = personaDAO.findPersonaByDni(dni)

        if (personaEncontrada != null) {
            // ¡Persona encontrada! Procedemos a la pantalla de cobro de actividad
            Toast.makeText(this, "Persona encontrada: ${personaEncontrada.nombre}", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, CobroActividadDatosActivity::class.java).apply {
                // Pasamos los datos de la persona a la siguiente pantalla
                putExtra("ID_PERSONA", personaEncontrada.idPersona)
                putExtra("DNI_PERSONA", personaEncontrada.dni)
                putExtra("NOMBRE_PERSONA", "${personaEncontrada.nombre} ${personaEncontrada.apellido}")
            }
            startActivity(intent)

        } else {
            // La persona no fue encontrada en la base de datos
            Toast.makeText(this, "No se encontró a ninguna persona con el DNI ingresado.", Toast.LENGTH_LONG).show()
        }
    }
}