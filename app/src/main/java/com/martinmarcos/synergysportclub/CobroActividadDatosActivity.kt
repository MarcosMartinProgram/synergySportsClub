package com.martinmarcos.synergysportclub

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class CobroActividadDatosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cobro_actividad_datos)

        val spinnerActividades = findViewById<Spinner>(R.id.spinnerActividades)
        val spinnerMetodoPago = findViewById<Spinner>(R.id.spinnerMetodoPago)
        val spinnerCuotas = findViewById<Spinner>(R.id.spinnerCuotas)

        // 2. Define la lista de opciones (igual que antes)
        val actividades = arrayOf("Zumba", "Boxeo", "Funcional", "Yoga", "Musculación")
        val metodoPago = arrayOf("Efectivo", "Tarjeta de crédito", "Tarjeta de débito")
        val cuotas = arrayOf("1", "3","6")

        // 3. Crea un ArrayAdapter (esto también es igual)
        // El primer layout es para la vista del spinner cuando está cerrado.
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, actividades)
        // El segundo layout es para la vista de la lista cuando se despliega.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, metodoPago)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val adapter3 = ArrayAdapter(this, android.R.layout.simple_spinner_item, cuotas)
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        // 4. Asigna el adapter a tu Spinner
        spinnerActividades.adapter = adapter
        spinnerMetodoPago.adapter = adapter2
        spinnerCuotas.adapter = adapter3


        // (Opcional) Escuchar cuándo el usuario selecciona un ítem
        spinnerActividades.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // 'parent.getItemAtPosition(position)' te da el ítem seleccionado
                val actividadSeleccionada = parent.getItemAtPosition(position).toString()
                Toast.makeText(
                    this@CobroActividadDatosActivity,
                    "Seleccionaste: $actividadSeleccionada",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }

        }
        val buttonAtrasMenu = findViewById<ImageButton>(R.id.buttonAtras)
        buttonAtrasMenu.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }

    }
}