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
import com.martinmarcos.synergysportclub.data.dao.UsuarioDAO

class LoginActivity : AppCompatActivity() {

    private lateinit var usuarioDAO: UsuarioDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usuarioDAO = UsuarioDAO(this)

        // capturamos los edittext
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            val usuario = etUsername.text.toString().trim()
            val contrasenia = etPassword.text.toString().trim()


            if(usuario.isEmpty() || contrasenia.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // --- LÓGICA DE LOGIN CORRECTA POR NOMBRE DE USUARIO ---

            // PASO 1: Buscar al usuario en la BD por su 'username'
            val usuarioEncontrado = usuarioDAO.getUsuarioPorUsername(usuario)

            if (usuarioEncontrado == null) {
                // Si el usuario es nulo, el 'username' no está registrado
                Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show()
            } else {
                // El usuario existe, ahora comparamos la contraseña
                if (usuarioEncontrado.pass == contrasenia) {
                    // ¡Éxito! El username y la contraseña coinciden
                    Toast.makeText(this, "¡Bienvenido ${usuarioEncontrado.username}!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MenuPrincipalActivity::class.java)
                    // Pasamos datos que puedan ser útiles en el menú
                    intent.putExtra("usuario_nombre", usuarioEncontrado.username)
                    intent.putExtra("usuario_id", usuarioEncontrado.idUsuario)
                    startActivity(intent)
                    finish() // Cerramos el login para que no se pueda volver con el botón "atrás"

                } else {
                    // El username es correcto, pero la contraseña no
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                }
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