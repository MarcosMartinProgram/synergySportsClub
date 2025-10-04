package com.martinmarcos.synergysportclub

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GestionValorCuotaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_valor_cuota)

        val btnActividad = findViewById<Button>(R.id.btnActividad)
        val btnValorActividad = findViewById<Button>(R.id.btnValorActividad)

        btnActividad.setOnClickListener {
            val intent = Intent(this, GestionActividadActivity::class.java)
            startActivity(intent)
        }

        btnValorActividad.setOnClickListener {
            val intent = Intent(this, GestionValorActividadActivity::class.java)
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