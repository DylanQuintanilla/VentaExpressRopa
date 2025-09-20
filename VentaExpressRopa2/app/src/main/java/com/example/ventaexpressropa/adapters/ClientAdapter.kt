package com.example.ventaexpressropa.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ventaexpressropa.models.Cliente
import com.example.ventaexpressropa.R
import android.widget.ImageButton

class ClientAdapter(
    private var lista: MutableList<Cliente>,
    private val listener: OnClientAction
) : RecyclerView.Adapter<ClientAdapter.ClientVH>() {

    interface OnClientAction {
        fun onEditar(cliente: Cliente)
        fun onEliminar(cliente: Cliente)
    }

    inner class ClientVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvCorreo: TextView = itemView.findViewById(R.id.tvCorreo)
        val tvTelefono: TextView = itemView.findViewById(R.id.tvTelefono)
        val btnEditar: ImageButton = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientVH {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClientVH(vista)
    }

    override fun onBindViewHolder(holder: ClientVH, position: Int) {
        val cliente = lista[position]
        holder.tvNombre.text = cliente.nombre
        holder.tvCorreo.text = "Correo: ${cliente.correo}"
        holder.tvTelefono.text = "Teléfono: ${cliente.telefono}"
        holder.btnEditar.setOnClickListener { listener.onEditar(cliente) }
        holder.btnEliminar.setOnClickListener { listener.onEliminar(cliente) }
    }

    override fun getItemCount(): Int = lista.size

    fun setData(nuevaLista: List<Cliente>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}