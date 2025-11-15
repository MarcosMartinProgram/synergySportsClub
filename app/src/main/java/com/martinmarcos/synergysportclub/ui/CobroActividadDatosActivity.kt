package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
// import android.widget.ImageButton // No se usa, se puede quitar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
// import androidx.activity.enableEdgeToEdge // No se usa, se quita
import androidx.appcompat.app.AppCompatActivity
// import androidx.compose.ui.semantics.text // Import incorrecto, se quita
// import androidx.glance.visibility // Import incorrecto, se quita
import com.martinmarcos.synergysportclub.R
import com.martinmarcos.synergysportclub.data.dao.ActividadDAO
import com.martinmarcos.synergysportclub.data.dao.PagoDAO
import com.martinmarcos.synergysportclub.data.dao.ValorActividadDAO
import com.martinmarcos.synergysportclub.model.Actividad

class CobroActividadDatosActivity : AppCompatActivity() {
    // Vistas de la UI (vinculadas a los IDs que acabamos de añadir)
    private lateinit var tvUsuarioDni: TextView
    private lateinit var tvUsuarioNombre: TextView
    private lateinit var spinnerActividades: Spinner
    private lateinit var tvDiasActividad: TextView
    private lateinit var tvHorariosActividad: TextView
    private lateinit var tvImporteActividad: TextView
    private lateinit var spinnerMetodoPago: Spinner
    private lateinit var buttonCobrarActividad: Button

    // DAOs
    private lateinit var actividadDAO: ActividadDAO
    private lateinit var valorActividadDAO: ValorActividadDAO
    private lateinit var pagoDAO: PagoDAO

    // Datos
    private var idPersona: Int = -1
    // --- CORRECCIÓN AQUÍ: Se quitan los '<' y '>' extra ---
    private var listaDeActividades: List<Actividad> = emptyList()
    private var montoActual: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobro_actividad_datos)

        inicializarComponentes()
        recibirDatosPersona()
        configurarSpinners()
        cargarActividadesEnSpinner()

        buttonCobrarActividad.setOnClickListener {
            registrarPagoActividad()
        }
    }

    private fun inicializarComponentes() {
        // DAOs
        actividadDAO = ActividadDAO(this)
        valorActividadDAO = ValorActividadDAO(this)
        pagoDAO = PagoDAO(this)

        // Vistas
        tvUsuarioDni = findViewById(R.id.tvUsuarioDni)
        tvUsuarioNombre = findViewById(R.id.tvUsuarioNombre)
        spinnerActividades = findViewById(R.id.spinnerActividades)
        tvDiasActividad = findViewById(R.id.tvDiasActividad)
        tvHorariosActividad = findViewById(R.id.tvHorariosActividad)
        tvImporteActividad = findViewById(R.id.tvImporteActividad)
        spinnerMetodoPago = findViewById(R.id.spinnerMetodoPago)
        buttonCobrarActividad = findViewById(R.id.buttonCobrarActividad)
    }

    private fun recibirDatosPersona() {
        idPersona = intent.getIntExtra("ID_PERSONA", -1)
        val dniPersona = intent.getStringExtra("DNI_PERSONA")
        val nombrePersona = intent.getStringExtra("NOMBRE_PERSONA")

        if (idPersona == -1) {
            Toast.makeText(this, "Error: No se recibió una persona válida.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        tvUsuarioDni.text = "Usuario (DNI): $dniPersona"
        tvUsuarioNombre.text = "Nombre: $nombrePersona"
    }

    private fun configurarSpinners() {
        // Spinner de Métodos de Pago
        val metodosPago = arrayOf("Efectivo", "Tarjeta de Débito", "Tarjeta de Crédito", "Transferencia")
        spinnerMetodoPago.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, metodosPago)

        // El Spinner de cuotas no aplica para el pago único de una actividad. Lo ocultamos.
        // Asumiendo que el TextView tiene el ID 'textView_cuotas_label' en tu XML

        findViewById<Spinner>(R.id.spinnerCuotas).visibility = View.GONE
    }

    private fun cargarActividadesEnSpinner() {
        listaDeActividades = actividadDAO.getAllActividades()

        if (listaDeActividades.isEmpty()) {
            Toast.makeText(this, "No hay actividades registradas en la base de datos.", Toast.LENGTH_LONG).show()
            buttonCobrarActividad.isEnabled = false
            return
        }

        val nombresActividades = listaDeActividades.map { it.nombre }
        spinnerActividades.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nombresActividades)

        spinnerActividades.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Cada vez que el usuario elige una actividad, actualizamos la info
                actualizarDetallesActividad(listaDeActividades[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Limpiamos los campos si no hay nada seleccionado
                limpiarDetalles()
            }
        }
    }

    private fun actualizarDetallesActividad(actividad: Actividad) {
        // Actualizamos los TextViews con los datos de la actividad seleccionada
        tvDiasActividad.text = "Días: ${actividad.dias}"
        tvHorariosActividad.text = "Horarios: ${actividad.horarios}"

        // Buscamos el precio de la actividad en la base de datos
        montoActual = valorActividadDAO.getMontoActualActividad(actividad.idActividad)

        if (montoActual != null) {
            tvImporteActividad.text = "Importe: $${"%.2f".format(montoActual)}"
            buttonCobrarActividad.isEnabled = true
        } else {
            tvImporteActividad.text = "Importe: (No definido)"
            buttonCobrarActividad.isEnabled = false // No se puede cobrar si no hay precio
            Toast.makeText(this, "Esta actividad no tiene un precio asignado.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarDetalles() {
        tvDiasActividad.text = "Días:"
        tvHorariosActividad.text = "Horarios:"
        tvImporteActividad.text = "Importe:"
        buttonCobrarActividad.isEnabled = false
    }

    private fun registrarPagoActividad() {
        if (idPersona == -1 || montoActual == null || listaDeActividades.isEmpty()) {
            Toast.makeText(this, "Error: Faltan datos para registrar el pago.", Toast.LENGTH_LONG).show()
            return
        }

        val actividadSeleccionada = listaDeActividades[spinnerActividades.selectedItemPosition]
        val concepto = "Pago de Actividad: ${actividadSeleccionada.nombre}"

        val resultadoId = pagoDAO.registrarPagoGenerico(idPersona, montoActual!!, concepto)

        if (resultadoId != -1L) {
            Toast.makeText(this, "¡Pago por '${actividadSeleccionada.nombre}' registrado con éxito!", Toast.LENGTH_LONG).show()
            // Volver al menú principal limpiando el historial
            val intent = Intent(this, MenuPrincipalActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Hubo un error al registrar el pago.", Toast.LENGTH_SHORT).show()
        }
    }
}