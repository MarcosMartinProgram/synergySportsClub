package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.martinmarcos.synergysportclub.R
import com.martinmarcos.synergysportclub.data.dao.PersonaDAO
import com.martinmarcos.synergysportclub.data.dao.UsuarioDAO

class CrearCuentaActivity : AppCompatActivity() {

    private lateinit var personaDAO: PersonaDAO
    private lateinit var usuarioDAO: UsuarioDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_cuenta)

        // Inicializar los DAOs
        personaDAO = PersonaDAO(this)
        usuarioDAO = UsuarioDAO(this)

        // Referencias a la UI (incluyendo los nuevos campos)
        val etName = findViewById<EditText>(R.id.etNameRegister)
        val etApellido = findViewById<EditText>(R.id.etApellidoRegister) // Nuevo
        val etUsername = findViewById<EditText>(R.id.etUsernameRegister)
        val etDni = findViewById<EditText>(R.id.etDniRegister) // Nuevo
        val etEmail = findViewById<EditText>(R.id.etEmailRegister)
        val etPassword = findViewById<EditText>(R.id.etPasswordRegister)
        val btnCrearCuenta = findViewById<Button>(R.id.buttonCrearCuenta)

        btnCrearCuenta.setOnClickListener {
            // Recogemos los datos de TODOS los campos
            val nombre = etName.text.toString().trim()
            val apellido = etApellido.text.toString().trim() // Nuevo
            val username = etUsername.text.toString().trim()
            val dni = etDni.text.toString().trim() // Nuevo
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // --- PASO 1: Validar que los campos no estén vacíos ---
            if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- PASO 2: Validar que no existan duplicados ---
            val personaExistente = personaDAO.getPersonaPorDni(dni)
            if (personaExistente != null) {
                Toast.makeText(this, "El DNI ya está registrado en el sistema.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val usuarioExistente = usuarioDAO.getUsuarioPorUsername(username)
            if (usuarioExistente != null) {
                Toast.makeText(this, "El nombre de usuario ya existe.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // --- PASO 3: Si todo es válido, crear la persona ---
            val idPersona = personaDAO.addPersona(
                nombre = nombre,
                apellido = apellido,
                dni = dni, // Usamos el DNI real
                fechaNacimiento = null, // Estos campos son opcionales para un operador
                domicilio = null,
                telefono = null,
                fichaMedica = null
            )

            if (idPersona > -1) {
                // Éxito, ahora creamos el usuario de la app
                // --- PASO 4: Crear el "Usuario" con el Rol de Administrador ---
                val idUsuario = usuarioDAO.addUsuario(
                    username = username,
                    mail = email,
                    pass = password, // En producción, cifrar la contraseña
                    idPersona = idPersona,
                    rolUsu = 1 // Rol 1 = Administrador/Operador
                )

                if (idUsuario > -1) {
                    Toast.makeText(this, "¡Cuenta de operador creada con éxito!", Toast.LENGTH_LONG).show()
                    finish() // Volver a la pantalla anterior
                } else {
                    Toast.makeText(this, "Error al crear la cuenta del operador.", Toast.LENGTH_LONG).show()
                    // Opcional: Borrar la persona recién creada para mantener la consistencia
                    // personaDAO.deletePersona(idPersona)
                }
            } else {
                Toast.makeText(this, "Error al crear el registro base de la persona.", Toast.LENGTH_LONG).show()
            }
        }

        val buttonAtrasMenu = findViewById<ImageButton>(R.id.buttonAtras)
        buttonAtrasMenu.setOnClickListener {
            // Es mejor usar finish() para volver a la pantalla anterior que crear un nuevo Intent
            finish()
        }
    }
}