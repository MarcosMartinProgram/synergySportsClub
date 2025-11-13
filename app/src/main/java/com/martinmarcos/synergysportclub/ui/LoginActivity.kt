// Archivo: ui/LoginActivity.kt (VERSIÓN CON BOTÓN ATRÁS FUNCIONAL)
package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton // <-- Importante añadir este import
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.martinmarcos.synergysportclub.R
import com.martinmarcos.synergysportclub.data.dao.PersonaDAO

class LoginActivity : AppCompatActivity() {

    private lateinit var personaDAO: PersonaDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Inicializamos el DAO
        personaDAO = PersonaDAO(this)

        // 2. Capturamos las vistas del layout
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textViewRegister = findViewById<TextView>(R.id.textViewRegister)

        // --- ¡AQUÍ ESTÁ LA SOLUCIÓN! ---
        // Buscamos el botón 'buttonAtras' que está dentro del header que incluiste
        val buttonAtras = findViewById<ImageButton>(R.id.buttonAtras)

        // 3. Configuramos los listeners (los "oyentes" de clics)

        // Listener para el botón de ir hacia atrás
        buttonAtras?.setOnClickListener {
            // Cierra la actividad actual y, como es la primera, sale de la app.
            finishAffinity()
        }

        // Listener para el botón de login
        buttonLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val idRol = personaDAO.validarUsuario(username, password)

            if (idRol != null) {
                // LOGIN EXITOSO
                Toast.makeText(this, "¡Bienvenido, $username!", Toast.LENGTH_SHORT).show()

                val personaLogueada = personaDAO.getPersonaPorUsername(username)
                val intent = Intent(this, MenuPrincipalActivity::class.java)

                if (personaLogueada != null) {
                    intent.putExtra("USER_ID", personaLogueada.idPersona)
                    intent.putExtra("USER_NOMBRE", personaLogueada.nombre)
                    intent.putExtra("USER_USERNAME", personaLogueada.username)
                } else {
                    intent.putExtra("USER_USERNAME", username)
                }

                startActivity(intent)
                finish() // Cerramos LoginActivity para que no se pueda volver a ella con el botón "atrás" del sistema
            } else {
                // LOGIN FALLIDO
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
            }
        }

        // Listener para el texto de registro
        textViewRegister.setOnClickListener {
            val intent = Intent(this, CrearCuentaActivity::class.java)
            startActivity(intent)
        }
    }
}
