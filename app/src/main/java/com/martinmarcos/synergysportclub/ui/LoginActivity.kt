package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.martinmarcos.synergysportclub.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // capturamos los edittext
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            val usuario = etUsername.text.toString()
            val contrasenia = etPassword.text.toString()

            if(usuario.isEmpty() || contrasenia.isEmpty()){
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }else if(usuario == "admin" && contrasenia == "1234"){
                val intent = Intent(this, MenuPrincipalActivity::class.java)
                intent.putExtra("usuario", usuario)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }



        }
        val textViewRegister = findViewById<TextView>(R.id.textViewRegister)
        textViewRegister.setOnClickListener {
            val intent1 = Intent(this, CrearCuentaActivity::class.java)
            startActivity(intent1)
        }
        val textViewForgotPassword = findViewById<TextView>(R.id.textViewForgotPassword)
        textViewForgotPassword.setOnClickListener {
            val intent1 = Intent(this, RecuperarContraseniaActivity::class.java)
            startActivity(intent1)
        }
        val buttonAtrasMenu = findViewById<ImageButton>(R.id.buttonAtras)
        buttonAtrasMenu.setOnClickListener {
            finishAffinity()
        }

    }
}