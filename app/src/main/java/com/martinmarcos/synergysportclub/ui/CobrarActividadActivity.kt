package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.martinmarcos.synergysportclub.R

class CobrarActividadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cobrar_actividad)

        val buttonBuscarNoSocio = findViewById<Button>(R.id.buttonBuscarNoSocio)
        buttonBuscarNoSocio.setOnClickListener {
            val intent = Intent(this, CobroActividadDatosActivity::class.java)
            startActivity(intent)
        }
        val buttonAtrasMenu = findViewById<ImageButton>(R.id.buttonAtras)
        buttonAtrasMenu.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }

    }
}