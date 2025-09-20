package com.example.ventaexpressropa.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.ventaexpressropa.R
import com.google.firebase.database.ValueEventListener
import com.example.ventaexpressropa.adapters.ProductAdapter
import com.example.ventaexpressropa.models.Producto

class ProductosActivity : AppCompatActivity(), ProductAdapter.OnProductAction {

    private lateinit var dbRef: DatabaseReference
    private lateinit var adapter: ProductAdapter

    private lateinit var tvVacio: TextView
    private lateinit var rvProductos: RecyclerView
    private lateinit var fabAgregar: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        dbRef = FirebaseDatabase.getInstance().reference.child("productos")

        tvVacio = findViewById(R.id.tvVacio)
        rvProductos = findViewById(R.id.rvProductos)
        fabAgregar = findViewById(R.id.fabAgregar)

        adapter = ProductAdapter(mutableListOf(), this)
        rvProductos.layoutManager = LinearLayoutManager(this)
        rvProductos.adapter = adapter

        fabAgregar.setOnClickListener {
            val intent = Intent(this, AddProductoActivity::class.java)
            startActivity(intent)
        }

        escucharCambios()
    }

    private fun escucharCambios() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = mutableListOf<Producto>()
                for (hijo in snapshot.children) {
                    val producto = hijo.getValue(Producto::class.java)
                    if (producto != null) lista.add(producto)
                }
                adapter.setData(lista)
                tvVacio.visibility = if (lista.isEmpty()) TextView.VISIBLE else TextView.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProductosActivity, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onEditar(producto: Producto) {
        val intent = Intent(this, AddProductoActivity::class.java)
        intent.putExtra(AddProductoActivity.EXTRA_ID, producto.id)
        intent.putExtra(AddProductoActivity.EXTRA_NOMBRE, producto.nombre)
        intent.putExtra(AddProductoActivity.EXTRA_DESCRIPCION, producto.descripcion)
        intent.putExtra(AddProductoActivity.EXTRA_PRECIO, producto.precio)
        intent.putExtra(AddProductoActivity.EXTRA_STOCK, producto.stock)
        startActivity(intent)
    }

    override fun onEliminar(producto: Producto) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Eliminar ${producto.nombre}?")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                producto.id?.let { dbRef.child(it).removeValue() }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}