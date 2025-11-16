package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton

import androidx.appcompat.widget.Toolbar

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.bumptech.glide.Glide
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
//import androidx.compose.ui.semantics.dismiss
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.martinmarcos.synergysportclub.R

class MenuPrincipalActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        val gifImageView1 = findViewById<ImageView>(R.id.gifImageView1)
        val gifImageView2 = findViewById<ImageView>(R.id.gifImageView2)
        val gifImageView3 = findViewById<ImageView>(R.id.gifImageView3)
        val gifImageView4 = findViewById<ImageView>(R.id.gifImageView4)
        val cornerRadius =  200


        Glide.with(this).asGif().load(R.drawable.list).transform(RoundedCorners(cornerRadius)).into(gifImageView1)
        Glide.with(this).asGif().load(R.drawable.cobact).transform(RoundedCorners(cornerRadius)).into(gifImageView2)
        Glide.with(this).asGif().load(R.drawable.gestion).transform(RoundedCorners(cornerRadius)).into(gifImageView3)
        Glide.with(this).asGif().load(R.drawable.cuota).transform(RoundedCorners(cornerRadius)).into(gifImageView4)

        gifImageView1.setOnClickListener {

            startActivity(Intent(this, RegistroUsuariosActivity::class.java))
        }

        gifImageView2.setOnClickListener {

            startActivity(Intent(this, CobrarCuotaActivity::class.java))
        }

        gifImageView3.setOnClickListener {

            startActivity(Intent(this, GestionActividadActivity::class.java))
        }

        gifImageView4.setOnClickListener {

            startActivity(Intent(this, CobrarActividadActivity::class.java))
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)

        // Configurar la toolbar como ActionBar
        setSupportActionBar(toolbar)

        // Ícono hamburguesa
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Manejar clicks en el menú
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.nav_inscribir -> {
                    startActivity(Intent(this, RegistroUsuariosActivity::class.java))
                }
                R.id.nav_cobrar_cuo -> {
                    startActivity(Intent(this, CobrarCuotaActivity::class.java))
                }
                R.id.nav_cobrar_act -> {
                    startActivity(Intent(this, CobrarActividadActivity::class.java))
                }
                R.id.nav_gestion -> {
                    startActivity(Intent(this, GestionActividadActivity::class.java))
                }
                R.id.nav_generar_carnet -> {
                    startActivity(Intent(this, GenerarCarnetActivity::class.java))
                }
                R.id.nav_listar_vencimientos -> {
                    startActivity(Intent(this, ListarVencimientosActivity::class.java))
                }
                /*R.id.nav_profile -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                }*/
                /*R.id.nav_about -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                }*/
                R.id.nav_logout -> {
                    // Al cerrar sesión, volvés al Login y cerrás el actual
                    AlertDialog.Builder(this)
                        .setTitle("Cerrar Sesión") // Título del diálogo
                        .setMessage("¿Estás seguro de que quieres cerrar la sesión?") // Mensaje de confirmación
                        .setCancelable(false) // El usuario debe presionar un botón

                        // Botón Positivo (confirmación)
                        .setPositiveButton("Sí") { dialog, which ->
                            // Si el usuario presiona "Sí", ejecuta el código de logout
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }

                        // Botón Negativo (cancelación)
                        .setNegativeButton("No") { dialog, which ->
                            // Si el usuario presiona "No", simplemente cierra el diálogo
                            dialog.dismiss()
                        }
                        .show()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
        val buttonAtrasMenu = findViewById<ImageButton>(R.id.buttonAtras)
        buttonAtrasMenu.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        val tvBienvenido = findViewById<TextView>(R.id.tvBienvenido)
        val usuario = intent.getStringExtra("USER_USERNAME") ?: "Usuario"
        tvBienvenido.text = "Bienvenido $usuario"

    }
}