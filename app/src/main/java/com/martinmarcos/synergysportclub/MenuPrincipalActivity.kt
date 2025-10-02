package com.martinmarcos.synergysportclub

import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.widget.Toolbar

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.bumptech.glide.Glide
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

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

        // 2. Cargar cada GIF
        Glide.with(this).asGif().load(R.drawable.list).transform(RoundedCorners(cornerRadius)).into(gifImageView1)
        Glide.with(this).asGif().load(R.drawable.cobact).transform(RoundedCorners(cornerRadius)).into(gifImageView2)
        Glide.with(this).asGif().load(R.drawable.gestion).transform(RoundedCorners(cornerRadius)).into(gifImageView3)
        Glide.with(this).asGif().load(R.drawable.cuota).transform(RoundedCorners(cornerRadius)).into(gifImageView4)

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
                R.id.nav_inscribir -> Toast.makeText(this, "Inscribir", Toast.LENGTH_SHORT).show()
                R.id.nav_cobrar_cuo -> Toast.makeText(this, "Cobrar cuota", Toast.LENGTH_SHORT).show()
                R.id.nav_cobrar_act -> Toast.makeText(this, "Cobrar actividad", Toast.LENGTH_SHORT).show()
                R.id.nav_gestion -> Toast.makeText(this, "Gestión", Toast.LENGTH_SHORT).show()
                R.id.nav_generar_carnet -> Toast.makeText(this, "Generar carnet", Toast.LENGTH_SHORT).show()
                R.id.nav_listar_vencimientos -> Toast.makeText(this, "Listar vencimientos", Toast.LENGTH_SHORT).show()

                R.id.nav_profile -> Toast.makeText(this, "Mi Perfil", Toast.LENGTH_SHORT).show()
                R.id.nav_about -> Toast.makeText(this, "Acerca de", Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> {
                    Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                }
                /*R.id.nav_inscribir -> {
                    startActivity(Intent(this, InscribirActivity::class.java))
                }
                R.id.nav_cobrar_cuo -> {
                    startActivity(Intent(this, CobrarCuotaActivity::class.java))
                }
                R.id.nav_cobrar_act -> {
                    startActivity(Intent(this, CobrarActividadActivity::class.java))
                }
                R.id.nav_gestion -> {
                    startActivity(Intent(this, GestionActivity::class.java))
                }
                R.id.nav_generar_carnet -> {
                    startActivity(Intent(this, GenerarCarnetActivity::class.java))
                }
                R.id.nav_listar_vencimientos -> {
                    startActivity(Intent(this, ListarVencimientosActivity::class.java))
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                }
                R.id.nav_about -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                }
                R.id.nav_logout -> {
                    // Al cerrar sesión, volvés al Login y cerrás el actual
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }*/
            }
            drawerLayout.closeDrawers()
            true
        }
    }
}