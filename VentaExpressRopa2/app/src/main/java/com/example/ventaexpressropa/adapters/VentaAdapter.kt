package com.example.ventaexpressropa.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ventaexpressropa.models.Venta
import com.example.ventaexpressropa.R
import android.widget.ImageButton
import java.text.SimpleDateFormat
import java.util.Date

class VentaAdapter(
    private var lista: MutableList<Venta>,
    private val listener: OnVentaAction
) : RecyclerView.Adapter<VentaAdapter.VentaVH>() {

    interface OnVentaAction {
        fun onEditar(venta: Venta)
        fun onEliminar(venta: Venta)
    }

    inner class VentaVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCliente: TextView = itemView.findViewById(R.id.tvCliente)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        val btnEditar: ImageButton = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentaVH {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venta, parent, false)
        return VentaVH(vista)
    }

    override fun onBindViewHolder(holder: VentaVH, position: Int) {
        val venta = lista[position]
        holder.tvCliente.text = venta.clienteNombre
        holder.tvTotal.text = "Total: $${venta.total}"
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        holder.tvFecha.text = sdf.format(Date(venta.fecha ?: 0))
        holder.btnEditar.setOnClickListener { listener.onEditar(venta) }
        holder.btnEliminar.setOnClickListener { listener.onEliminar(venta) }
    }

    override fun getItemCount(): Int = lista.size

    fun setData(nuevaLista: List<Venta>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}