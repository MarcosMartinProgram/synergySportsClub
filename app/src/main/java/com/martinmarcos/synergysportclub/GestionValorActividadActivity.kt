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
        setContentView(R.layout.activity_gestion_valor_actividad)

        val btnActividad = findViewById<Button>(R.id.btnActividad)
        val btnValorCuota = findViewById<Button>(R.id.btnValorCuota)

        btnActividad.setOnClickListener {
            val intent = Intent(this, GestionValorActividadActivity::class.java)
            startActivity(intent)
        }

        btnValorCuota.setOnClickListener {
            val intent = Intent(this, GestionValorCuotaActivity::class.java)
            startActivity(intent)
        }

    }
}