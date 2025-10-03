package com.martinmarcos.synergysportclub

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.content.Intent

class GestionActividadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestionactividad)

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

    }
}