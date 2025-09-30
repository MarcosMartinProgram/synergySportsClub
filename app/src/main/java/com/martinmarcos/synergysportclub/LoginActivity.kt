package com.martinmarcos.synergysportclub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }
        val textViewRegister = findViewById<TextView>(R.id.textViewRegister)
        textViewRegister.setOnClickListener {
            val intent1 = Intent(this, CrearCuentaActivity::class.java)
            startActivity(intent1)
        }

    }
}