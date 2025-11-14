// Archivo: ui/CrearCuentaActivity.kt (VERSIÓN FINAL CORREGIDA)
package com.martinmarcos.synergysportclub.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.martinmarcos.synergysportclub.R
import com.martinmarcos.synergysportclub.data.dao.PersonaDAO
import java.text.SimpleDateFormat
import java.util.*

class CrearCuentaActivity : AppCompatActivity() {

    private lateinit var personaDAO: PersonaDAO
    private lateinit var etName: EditText
    private lateinit var etApellido: EditText
    private lateinit var etDni: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnCrearCuenta: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)

        // Inicializar DAO
        personaDAO = PersonaDAO(this)

        // Inicializar Vistas
        setupViews()

        // Configurar Listeners
        btnCrearCuenta.setOnClickListener {
            registrarAdministrador()
        }
    }

    private fun setupViews() {
        etName = findViewById(R.id.etNameRegister)
        etApellido = findViewById(R.id.etApellidoRegister)
        etDni = findViewById(R.id.etDniRegister)
        etUsername = findViewById(R.id.etUsernameRegister)
        // Tu layout de crear cuenta podría no tener email, pero si lo tiene, aquí se referencia
        // val etEmail = findViewById<EditText>(R.id.etEmailRegister)
        etPassword = findViewById(R.id.etPasswordRegister)
        btnCrearCuenta = findViewById(R.id.buttonCrearCuenta)

        // Botón para volver atrás
        findViewById<ImageButton>(R.id.buttonAtras)?.setOnClickListener {
            finish()
        }
    }

    private fun registrarAdministrador() {
        // --- Recoger datos ---
        val nombre = etName.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val dni = etDni.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // --- Validaciones ---
        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        val fechaAlta = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        // --- LLAMADA A LA BASE DE DATOS (VERSIÓN CORREGIDA Y COMPLETA) ---
        // Esta es la línea que soluciona tus errores.
        // Ahora pasamos TODOS los parámetros que 'addPersona' espera.
        val nuevoId = personaDAO.addPersona(
            nombre = nombre,
            apellido = apellido,
            dni = dni,
            idRol = 1, // Rol Fijo: 1 = Administrador
            username = username,
            pass = password,
            fechaAlta = fechaAlta,
            telefono = null,         // Valor por defecto para los nuevos campos
            email = null,            // Valor por defecto para los nuevos campos
            domicilio = null,        // Valor por defecto para los nuevos campos
            fechaNacimiento = null,  // Valor por defecto para los nuevos campos
            fichaMedica = false      // Valor por defecto para los nuevos campos
        )

        // --- Informar resultado ---
        if (nuevoId > -1) {
            Toast.makeText(this, "¡Cuenta de Administrador creada con éxito!", Toast.LENGTH_LONG).show()
            finish() // Volver a la pantalla de login
        } else {
            Toast.makeText(this, "Error: El DNI o el nombre de usuario ya existen.", Toast.LENGTH_LONG).show()
        }
    }
}
