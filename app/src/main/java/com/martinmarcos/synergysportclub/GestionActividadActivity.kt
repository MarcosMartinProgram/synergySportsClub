package com.martinmarcos.synergysportclub

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.content.Intent
import android.widget.ImageButton
import android.widget.Toast

class GestionActividadActivity : AppCompatActivity() {
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_actividad)

        val btnValorActividad = findViewById<Button>(R.id.btnValorActividad)
        val btnValorCuota = findViewById<Button>(R.id.btnValorCuota)

        btnValorActividad.setOnClickListener {
            val intent = Intent(this, GestionValorActividadActivity::class.java)
            startActivity(intent)
        }

        btnValorCuota.setOnClickListener {
            val intent = Intent(this, GestionValorCuotaActivity::class.java)
            startActivity(intent)
        }

        val buttonAtrasMenu = findViewById<ImageButton>(R.id.buttonAtras)
        buttonAtrasMenu.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }

        val buttonSalir = findViewById<Button>(R.id.btnSalir)
        buttonSalir.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }


        val buttonGrabar = findViewById<Button>(R.id.btnGrabar)
        buttonGrabar.setOnClickListener {
            Toast.makeText(this, "GRABAR OK!", Toast.LENGTH_SHORT).show()
        }


    }
}