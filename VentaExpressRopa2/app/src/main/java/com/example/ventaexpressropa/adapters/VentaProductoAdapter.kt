package com.example.ventaexpressropa.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ventaexpressropa.models.ProductoVenta
import com.example.ventaexpressropa.R

class VentaProductoAdapter(
    private var lista: MutableList<ProductoVenta>
) : RecyclerView.Adapter<VentaProductoAdapter.VentaProductoVH>() {

    inner class VentaProductoVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)
        val tvSubtotal: TextView = itemView.findViewById(R.id.tvSubtotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentaProductoVH {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venta_producto, parent, false)
        return VentaProductoVH(vista)
    }

    override fun onBindViewHolder(holder: VentaProductoVH, position: Int) {
        val item = lista[position]
        holder.tvNombre.text = item.nombre
        holder.tvCantidad.text = "Cantidad: ${item.cantidad}"
        holder.tvSubtotal.text = "Subtotal: $${(item.cantidad ?: 0) * (item.precioUnitario ?: 0.0)}"
    }

    override fun getItemCount(): Int = lista.size

    fun addItem(item: ProductoVenta) {
        lista.add(item)
        notifyItemInserted(lista.size - 1)
    }

    fun setData(nuevaLista: List<ProductoVenta>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}