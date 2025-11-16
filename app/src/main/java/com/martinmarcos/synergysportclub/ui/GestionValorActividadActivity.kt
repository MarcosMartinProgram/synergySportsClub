package com.martinmarcos.synergysportclub.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import com.martinmarcos.synergysportclub.R
import com.martinmarcos.synergysportclub.data.dao.ActividadDAO
import com.martinmarcos.synergysportclub.data.dao.ValorActividadDAO
import com.martinmarcos.synergysportclub.model.Actividad
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GestionValorActividadActivity : AppCompatActivity() {

    // DAOs para acceder a las tablas
    private lateinit var actividadDAO: ActividadDAO
    private lateinit var valorActividadDAO: ValorActividadDAO

    // Vistas del Layout
    private lateinit var spinnerActividades: Spinner
    private lateinit var etFechaValor: EditText
    private lateinit var etValorActividad: EditText
    private lateinit var btnGrabar: Button
    private lateinit var btnAtras: ImageButton

    // Lista para guardar las actividades que vienen de la BBDD
    private var listaActividades: List<Actividad> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_valor_actividad)

        // 1. Inicializar los DAOs y las Vistas
        actividadDAO = ActividadDAO(this)
        valorActividadDAO = ValorActividadDAO(this)

        spinnerActividades = findViewById(R.id.spinnerActividades)
        etFechaValor = findViewById(R.id.etFechaValor)
        etValorActividad = findViewById(R.id.etValorActividad)
        btnGrabar = findViewById(R.id.btnGrabar)

        // 2. Cargar las actividades en el Spinner
        cargarActividadesEnSpinner()

        // 3. Configurar el botón de Grabar
        btnGrabar.setOnClickListener {
            guardarValorActividad()
        }
        btnAtras = findViewById(R.id.buttonAtras)
        btnAtras.setOnClickListener {

            finish()
        }


        configurarNavegacion()
    }

    private fun cargarActividadesEnSpinner() {
        // Obtenemos todas las actividades de la BBDD usando el DAO
        listaActividades = actividadDAO.getAllActividades()

        // Creamos una lista de Strings solo con los nombres para mostrar en el Spinner
        val nombresActividades = listaActividades.map { it.nombre }

        // Creamos un adaptador para el Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresActividades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asignamos el adaptador al Spinner
        spinnerActividades.adapter = adapter
    }

    private fun guardarValorActividad() {
        // Validar que se haya seleccionado una actividad
        if (listaActividades.isEmpty() || spinnerActividades.selectedItemPosition < 0) {
            Toast.makeText(this, "No hay actividades para seleccionar o no se ha elegido ninguna", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener la actividad seleccionada
        val actividadSeleccionada = listaActividades[spinnerActividades.selectedItemPosition]
        val idActividad = actividadSeleccionada.idActividad

        // Obtener y validar el valor
        val montoStr = etValorActividad.text.toString()
        if (montoStr.isBlank()) {
            etValorActividad.error = "El valor no puede estar vacío"
            return
        }
        val monto = montoStr.toDoubleOrNull()
        if (monto == null || monto <= 0) {
            etValorActividad.error = "Ingrese un valor numérico positivo"
            return
        }

        // Obtener la fecha (si está vacía, usar la de hoy)
        var fecha = etFechaValor.text.toString()
        if (fecha.isBlank()) {
            fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        }

        // Guardar en la base de datos usando el DAO
        val idGenerado = valorActividadDAO.addValorActividad(idActividad, monto, fecha)

        if (idGenerado != -1L) {
            Toast.makeText(this, "Valor para '${actividadSeleccionada.nombre}' guardado con éxito", Toast.LENGTH_LONG).show()
            // Limpiar campos
            etValorActividad.text.clear()
            etFechaValor.text.clear()
            spinnerActividades.setSelection(0)
        } else {
            Toast.makeText(this, "Error al guardar el valor", Toast.LENGTH_LONG).show()
        }
    }

    private fun configurarNavegacion() {
        val btnActividad = findViewById<Button>(R.id.btnActividad)
        val btnValorCuota = findViewById<Button>(R.id.btnValorCuota)
        val buttonAtrasMenu = findViewById<ImageButton>(R.id.buttonAtras)
        val buttonSalir = findViewById<Button>(R.id.btnSalir)

        btnActividad.setOnClickListener {
            startActivity(Intent(this, GestionActividadActivity::class.java))
        }

        btnValorCuota.setOnClickListener {
            startActivity(Intent(this, GestionValorCuotaActivity::class.java))
        }

        buttonAtrasMenu.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
        }

        buttonSalir.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
        }
    }
}

