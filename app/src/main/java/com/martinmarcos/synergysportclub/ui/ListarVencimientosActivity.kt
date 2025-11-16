// ruta: src/main/java/com/martinmarcos/synergysportclub/ui/ListarVencimientosActivity.kt
package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.martinmarcos.synergysportclub.R
import com.martinmarcos.synergysportclub.data.dao.PagoSocioDAO
import com.martinmarcos.synergysportclub.ui.adapter.VencimientosAdapter

class ListarVencimientosActivity : AppCompatActivity() {

    private lateinit var editTextFecha: EditText
    private lateinit var buttonListar: Button
    private lateinit var recyclerViewVencimientos: RecyclerView
    private lateinit var tvResultadosLabel: TextView

    private lateinit var pagoSocioDAO: PagoSocioDAO
    private lateinit var vencimientosAdapter: VencimientosAdapter
    private lateinit var btnAtras: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_vencimientos)

        // 1. Inicializar componentes
        pagoSocioDAO = PagoSocioDAO(this)
        editTextFecha = findViewById(R.id.editTextFecha)
        buttonListar = findViewById(R.id.buttonListar)
        recyclerViewVencimientos = findViewById(R.id.recyclerViewVencimientos)
        tvResultadosLabel = findViewById(R.id.tvResultadosLabel)

        // 2. Configurar el RecyclerView
        setupRecyclerView()

        // 3. Configurar el listener del botón
        buttonListar.setOnClickListener {
            buscarVencimientos()
        }
        btnAtras = findViewById(R.id.buttonAtras)
        btnAtras.setOnClickListener {

            finish()
        }

    }

    private fun setupRecyclerView() {
        vencimientosAdapter = VencimientosAdapter(emptyList()) // Inicialmente vacío
        recyclerViewVencimientos.adapter = vencimientosAdapter
        recyclerViewVencimientos.layoutManager = LinearLayoutManager(this)
    }

    private fun buscarVencimientos() {
        val fecha = editTextFecha.text.toString().trim()

        if (fecha.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese una fecha.", Toast.LENGTH_SHORT).show()
            return
        }

        // Aquí podrías añadir validación para el formato de la fecha AAAA-MM-DD

        // Realizamos la búsqueda en la base de datos
        val listaVencimientos = pagoSocioDAO.findVencimientosByDate(fecha)

        // Mostramos la etiqueta de resultados y actualizamos el adapter
        tvResultadosLabel.visibility = View.VISIBLE
        vencimientosAdapter.updateData(listaVencimientos)

        if (listaVencimientos.isEmpty()) {
            Toast.makeText(this, "No se encontraron vencimientos para esa fecha.", Toast.LENGTH_SHORT).show()
        }
    }

}
