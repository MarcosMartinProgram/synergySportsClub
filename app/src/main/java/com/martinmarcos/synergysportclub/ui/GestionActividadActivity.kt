// Archivo: GestionActividadActivity.kt
package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.martinmarcos.synergysportclub.R
import com.martinmarcos.synergysportclub.data.dao.ActividadDAO

class GestionActividadActivity : AppCompatActivity() {

    private lateinit var actividadDAO: ActividadDAO
    private lateinit var etNombre: EditText
    private lateinit var etHorario: EditText
    private lateinit var etCupo: EditText
    private lateinit var cbLunes: CheckBox
    private lateinit var cbMartes: CheckBox
    private lateinit var cbMiercoles: CheckBox
    private lateinit var cbJueves: CheckBox
    private lateinit var cbViernes: CheckBox
    private lateinit var cbSabado: CheckBox
    private lateinit var cbDomingo: CheckBox
    private lateinit var btnGrabar: Button
    private lateinit var btnValorActividad: Button
    private lateinit var btnValorCuota: Button
    private lateinit var buttonAtrasMenu: ImageButton
    private lateinit var buttonSalir: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_actividad)

        // DAO y Vistas
        actividadDAO = ActividadDAO(this)
        etNombre = findViewById(R.id.etNombre)
        etHorario = findViewById(R.id.etHorario)
        etCupo = findViewById(R.id.etCupo)
        cbLunes = findViewById(R.id.cbLunes)
        cbMartes = findViewById(R.id.cbMartes)
        cbMiercoles = findViewById(R.id.cbMiercoles)
        cbJueves = findViewById(R.id.cbJueves)
        cbViernes = findViewById(R.id.cbViernes)
        cbSabado = findViewById(R.id.cbSabado)
        cbDomingo = findViewById(R.id.cbDomingo)
        btnGrabar = findViewById(R.id.btnGrabar)
        btnValorActividad = findViewById(R.id.btnValorActividad)
        btnValorCuota = findViewById(R.id.btnValorCuota)
        buttonAtrasMenu = findViewById(R.id.buttonAtras)
        buttonSalir = findViewById(R.id.btnSalir)


        // --- BOTONES ---
        btnGrabar.setOnClickListener {
            guardarNuevaActividad()
        }

        btnValorActividad.setOnClickListener {
            val intent = Intent(this, GestionValorActividadActivity::class.java)
            startActivity(intent)
        }

        btnValorCuota.setOnClickListener {
            val intent = Intent(this, GestionValorCuotaActivity::class.java)
            startActivity(intent)
        }

        buttonAtrasMenu.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }

        buttonSalir.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }
    }

    private fun guardarNuevaActividad() {

        val nombre = etNombre.text.toString().trim()
        val horarios = etHorario.text.toString().trim()
        val cupoStr = etCupo.text.toString().trim()
        val diasSeleccionados = obtenerDiasSeleccionados()


        if (nombre.isEmpty() || horarios.isEmpty() || cupoStr.isEmpty() || diasSeleccionados.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación y conversión del cupo a número
        val cupo = cupoStr.toIntOrNull()
        if (cupo == null) {
            Toast.makeText(this, "El cupo debe ser un número válido", Toast.LENGTH_SHORT).show()
            etCupo.error = "Número inválido"
            return
        }

        // Llamada al DAO para insertar los datos
        val idGenerado = actividadDAO.addActividad(nombre, horarios, diasSeleccionados, cupo)

        // Informar al usuario y limpiar el formulario
        if (idGenerado != -1L) {
            Toast.makeText(this, "Actividad '$nombre' guardada con éxito", Toast.LENGTH_LONG).show()
            limpiarFormulario()
        } else {
            Toast.makeText(this, "Error al guardar la actividad en la base de datos.", Toast.LENGTH_LONG).show()
        }
    }

    private fun obtenerDiasSeleccionados(): String {
        val dias = mutableListOf<String>()
        if (cbLunes.isChecked) dias.add("Lunes")
        if (cbMartes.isChecked) dias.add("Martes")
        if (cbMiercoles.isChecked) dias.add("Miércoles")
        if (cbJueves.isChecked) dias.add("Jueves")
        if (cbViernes.isChecked) dias.add("Viernes")
        if (cbSabado.isChecked) dias.add("Sábado")
        if (cbDomingo.isChecked) dias.add("Domingo")
        return dias.joinToString(", ")
    }

    private fun limpiarFormulario() {
        etNombre.text.clear()
        etHorario.text.clear()
        etCupo.text.clear()
        cbLunes.isChecked = false
        cbMartes.isChecked = false
        cbMiercoles.isChecked = false
        cbJueves.isChecked = false
        cbViernes.isChecked = false
        cbSabado.isChecked = false
        cbDomingo.isChecked = false
        etNombre.requestFocus()
    }
}
