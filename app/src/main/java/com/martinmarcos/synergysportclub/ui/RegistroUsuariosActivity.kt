package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.martinmarcos.synergysportclub.R
import com.martinmarcos.synergysportclub.data.dao.PersonaDAO
import com.martinmarcos.synergysportclub.data.dao.RolDAO
import com.martinmarcos.synergysportclub.model.Rol
import com.martinmarcos.synergysportclub.ui.MenuPrincipalActivity
import java.text.SimpleDateFormat
import java.util.*

class RegistroUsuariosActivity : AppCompatActivity() {

    // Declaraciones de tus vistas (esto no cambia)
    private lateinit var personaDAO: PersonaDAO
    private lateinit var rolDAO: RolDAO
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etDNI: EditText
    private lateinit var spinnerRolCliente: Spinner
    private lateinit var etFechaNacimiento: EditText
    private lateinit var etDomicilio: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etEmail: EditText
    private lateinit var chkFichaMedica: CheckBox
    private lateinit var btnInscribir: Button
    private var listaRoles: List<Rol> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuarios)

        // Inicialización estándar
        personaDAO = PersonaDAO(this)
        rolDAO = RolDAO(this)
        setupViews()
        cargarRolesEnSpinner()
        setupListeners()
    }

    private fun setupViews() {
        etNombre = findViewById(R.id.editTextNombre)
        etApellido = findViewById(R.id.editTextApellido)
        etDNI = findViewById(R.id.editNumberDNI)
        spinnerRolCliente = findViewById(R.id.spinnerRolCliente)
        etFechaNacimiento = findViewById(R.id.editFechaNacimiento)
        etDomicilio = findViewById(R.id.editTextPostal)
        etTelefono = findViewById(R.id.editNumberTelefono)
        etEmail = findViewById(R.id.editTextEmail)
        chkFichaMedica = findViewById(R.id.checkboxTerminos)
        btnInscribir = findViewById(R.id.buttonRecuperar)
    }

    private fun setupListeners() {
        // Listener para el botón de inscribir (esto está bien)
        btnInscribir.setOnClickListener {
            registrarNuevaPersona()
        }

        // --- ¡ESTA ES LA ÚNICA LÓGICA DE BOTÓN "ATRÁS" QUE NECESITAMOS! ---
        // Lógica para el nuevo botón de "Atrás" en el HEADER
        val buttonAtrasHeader = findViewById<ImageButton>(R.id.buttonAtrasHeader)
        buttonAtrasHeader.setOnClickListener {
            // Cierra esta pantalla y vuelve a la anterior. Es la forma más limpia.
            finish()
        }

        // El código que buscaba el botón "buttonAtras" del footer ha sido eliminado,
        // solucionando así el crash.
    }

    private fun cargarRolesEnSpinner() {
        listaRoles = rolDAO.getAllRoles()
        if (listaRoles.isNotEmpty()) {
            val nombresRoles = listaRoles.map { it.nombreRol }.toMutableList()
            nombresRoles.add(0, "Elige una opción")
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresRoles)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerRolCliente.adapter = adapter
            spinnerRolCliente.setSelection(0)
        } else {
            Toast.makeText(this, "Error: No se pudieron cargar los roles.", Toast.LENGTH_LONG).show()
        }
    }

    private fun registrarNuevaPersona() {
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val dni = etDNI.text.toString().trim()

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
            Toast.makeText(this, "Nombre, Apellido y DNI son obligatorios.", Toast.LENGTH_LONG).show()
            return
        }
        if (spinnerRolCliente.selectedItemPosition == 0) {
            Toast.makeText(this, "Debe seleccionar un tipo de cliente.", Toast.LENGTH_SHORT).show()
            return
        }
        val rolSeleccionado = listaRoles[spinnerRolCliente.selectedItemPosition - 1]
        val fechaAlta = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        val nuevoId = personaDAO.addPersona(
            nombre = nombre,
            apellido = apellido,
            dni = dni,
            idRol = rolSeleccionado.idRol,
            username = null,
            pass = null,
            fechaAlta = fechaAlta,
            telefono = etTelefono.text.toString().trim().ifEmpty { null },
            email = etEmail.text.toString().trim().ifEmpty { null },
            domicilio = etDomicilio.text.toString().trim().ifEmpty { null },
            fechaNacimiento = etFechaNacimiento.text.toString().trim().ifEmpty { null },
            fichaMedica = chkFichaMedica.isChecked
        )

        if (nuevoId > -1) {
            Toast.makeText(this, "${rolSeleccionado.nombreRol} '$nombre' inscripto con éxito!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Error: El DNI ya existe en el sistema.", Toast.LENGTH_LONG).show()
        }
    }
}
