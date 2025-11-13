// Archivo: ui/MenuPrincipalActivity.kt (VERSIÓN CORREGIDA Y FINAL)
package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.navigation.NavigationView
import com.martinmarcos.synergysportclub.R

class MenuPrincipalActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        // --- PASO 1: RECUPERAR LOS DATOS Y PONER EL MENSAJE DE BIENVENIDA ---
        // ¡Esta es la corrección principal!
        val nombreUsuario = intent.getStringExtra("USER_NOMBRE") ?: "Usuario" // Usamos la clave correcta: "USER_NOMBRE"
        val tvBienvenido = findViewById<TextView>(R.id.tvBienvenido)
        tvBienvenido.text = "Bienvenido, $nombreUsuario"

        // --- El resto de tu código, que ya funciona, se queda igual ---

        // Configuración de los GIFs
        setupGifButtons()

        // Configuración del Navigation Drawer
        setupNavigationDrawer()
    }

    private fun setupGifButtons() {
        val gifImageView1 = findViewById<ImageView>(R.id.gifImageView1)
        val gifImageView2 = findViewById<ImageView>(R.id.gifImageView2)
        val gifImageView3 = findViewById<ImageView>(R.id.gifImageView3)
        val gifImageView4 = findViewById<ImageView>(R.id.gifImageView4)
        val cornerRadius = 200

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
    }

    private fun setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_inscribir -> startActivity(Intent(this, RegistroUsuariosActivity::class.java))
                R.id.nav_cobrar_cuo -> startActivity(Intent(this, CobrarCuotaActivity::class.java))
                R.id.nav_cobrar_act -> startActivity(Intent(this, CobrarActividadActivity::class.java))
                R.id.nav_gestion -> startActivity(Intent(this, GestionActividadActivity::class.java))
                R.id.nav_generar_carnet -> startActivity(Intent(this, GenerarCarnetActivity::class.java))
                R.id.nav_listar_vencimientos -> startActivity(Intent(this, ListarVencimientosActivity::class.java))
                R.id.nav_logout -> showLogoutDialog()
            }
            drawerLayout.closeDrawers()
            true
        }

        // El botón de "Atrás" en el header del menú principal no debería volver al Login.
        // Si el usuario quiere salir, debe usar el botón de logout.
        // Por eso, he eliminado la lógica de ese botón para evitar confusiones.
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que quieres cerrar la sesión?")
            .setCancelable(false)
            .setPositiveButton("Sí") { _, _ ->
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
