package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.martinmarcos.synergysportclub.R

class RegistroUsuariosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_usuarios)

        val switchEsSocio = findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.switchEsSocio)

        // Establecer el texto inicial basado en si está marcado o no (por si acaso se restaura el estado)
        if (switchEsSocio.isChecked) {
            switchEsSocio.text = "Socio"
        } else {
            switchEsSocio.text = "No Socio"
        }

        // Añadir un listener para cambiar el texto cuando el usuario interactúe
        switchEsSocio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // El Switch está activado (a la derecha)
                switchEsSocio.text = "Socio"
            } else {
                // El Switch está desactivado (a la izquierda)
                switchEsSocio.text = "No Socio"
            }
        }



        val buttonInscribir = findViewById<Button>(R.id.buttonRecuperar)
        buttonInscribir.setOnClickListener {
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