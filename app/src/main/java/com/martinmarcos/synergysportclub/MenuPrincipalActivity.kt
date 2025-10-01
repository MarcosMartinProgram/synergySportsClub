package com.martinmarcos.synergysportclub

import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.widget.Toolbar

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MenuPrincipalActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

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
                    Toast.makeText(this, "Inicio", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_cobrar_cuo-> {
                    Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_cobrar_act -> {
                    Toast.makeText(this, "Configuración", Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }
}