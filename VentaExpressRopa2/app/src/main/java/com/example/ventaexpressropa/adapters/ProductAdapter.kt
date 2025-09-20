package com.example.ventaexpressropa.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ventaexpressropa.models.Producto
import com.example.ventaexpressropa.R
import android.widget.ImageButton

class ProductAdapter(
    private var lista: MutableList<Producto>,
    private val listener: OnProductAction
) : RecyclerView.Adapter<ProductAdapter.ProductVH>() {

    interface OnProductAction {
        fun onEditar(producto: Producto)
        fun onEliminar(producto: Producto)
    }

    inner class ProductVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
        val tvStock: TextView = itemView.findViewById(R.id.tvStock)
        val btnEditar: ImageButton = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductVH(vista)
    }

    override fun onBindViewHolder(holder: ProductVH, position: Int) {
        val producto = lista[position]
        holder.tvNombre.text = producto.nombre
        holder.tvDescripcion.text = producto.descripcion
        holder.tvPrecio.text = "Precio: $${producto.precio}"
        holder.tvStock.text = "Stock: ${producto.stock}"
        holder.btnEditar.setOnClickListener { listener.onEditar(producto) }
        holder.btnEliminar.setOnClickListener { listener.onEliminar(producto) }
    }

    override fun getItemCount(): Int = lista.size

    fun setData(nuevaLista: List<Producto>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}