package com.martinmarcos.synergysportclub

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.content.Intent

class GestionValorActividadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestionvaloractividad)

        val btnActividad = findViewById<Button>(R.id.btnActividad)
        val btnValorCuota = findViewById<Button>(R.id.btnValorCuota)

        btnActividad.setOnClickListener {
            val intent = Intent(this, ValorActividad::class.java)
            startActivity(intent)
        }

        btnValorCuota.setOnClickListener {
            val intent = Intent(this, GestionValorCuota::class.java)
            startActivity(intent)
        }

    }
}