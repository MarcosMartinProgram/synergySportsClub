package com.martinmarcos.synergysportclub

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.martinmarcos.synergysportclub.CobroActividadDatosActivity

class CobrarCuotaDatosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cobrar_cuota_datos)

        val spinnerMetodoPago = findViewById<Spinner>(R.id.spinnerMetodoPago)
        val spinnerCuotas = findViewById<Spinner>(R.id.spinnerCuotas)

        // 2. Define la lista de opciones (igual que antes)

        val metodoPago = arrayOf("Efectivo", "Tarjeta de crédito", "Tarjeta de débito")
        val cuotas = arrayOf("1", "3","6")

        // 3. Crea un ArrayAdapter (esto también es igual)
        // El primer layout es para la vista del spinner cuando está cerrado.

        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, metodoPago)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val adapter3 = ArrayAdapter(this, android.R.layout.simple_spinner_item, cuotas)
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        // 4. Asigna el adapter a tu Spinner

        spinnerMetodoPago.adapter = adapter2
        spinnerCuotas.adapter = adapter3

        val buttonCobroCuota = findViewById<Button>(R.id.buttonCobroCuota)
                buttonCobroCuota.setOnClickListener {
                    val intent = Intent(this, MenuPrincipalActivity::class.java)
                    startActivity(intent)
                }


        val buttonAtrasMenu = findViewById<ImageButton>(R.id.buttonAtras)
        buttonAtrasMenu.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }

    }
}