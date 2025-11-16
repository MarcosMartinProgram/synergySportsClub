// ruta: src/main/java/com/martinmarcos/synergysportclub/ui/adapter/VencimientosAdapter.kt
package com.martinmarcos.synergysportclub.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.martinmarcos.synergysportclub.R
import com.martinmarcos.synergysportclub.model.VencimientoSocio

class VencimientosAdapter(private var vencimientos: List<VencimientoSocio>) : RecyclerView.Adapter<VencimientosAdapter.VencimientoViewHolder>() {

    // ViewHolder: Representa una única fila en la lista
    class VencimientoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombreSocio: TextView = view.findViewById(R.id.tvNombreSocio)
        val tvDniSocio: TextView = view.findViewById(R.id.tvDniSocio)
        val tvMontoPagado: TextView = view.findViewById(R.id.tvMontoPagado)
    }

    // Crea una nueva vista (fila) para un item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VencimientoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vencimiento, parent, false)
        return VencimientoViewHolder(view)
    }

    // Vincula los datos de un item con su vista
    override fun onBindViewHolder(holder: VencimientoViewHolder, position: Int) {
        val vencimiento = vencimientos[position]
        holder.tvNombreSocio.text = "${vencimiento.apellido}, ${vencimiento.nombre}"
        holder.tvDniSocio.text = "DNI: ${vencimiento.dni}"
        holder.tvMontoPagado.text = "Pagó: $${"%.2f".format(vencimiento.monto)}"
    }

    // Devuelve el número total de items en la lista
    override fun getItemCount() = vencimientos.size

    // Función para actualizar la lista de datos y notificar al adapter
    fun updateData(newVencimientos: List<VencimientoSocio>) {
        vencimientos = newVencimientos
        notifyDataSetChanged() // Refresca la lista
    }
}

