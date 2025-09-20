package com.example.ventaexpressropa.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.ventaexpressropa.R
import com.example.ventaexpressropa.adapters.VentaAdapter
import com.example.ventaexpressropa.models.Producto
import com.example.ventaexpressropa.models.Venta

class VentasActivity : AppCompatActivity(), VentaAdapter.OnVentaAction {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var dbRef: DatabaseReference
    private val dbRefProductos = FirebaseDatabase.getInstance().reference.child("productos")
    private lateinit var adapter: VentaAdapter

    private lateinit var tvVacio: TextView
    private lateinit var rvVentas: RecyclerView
    private lateinit var fabAgregar: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ventas)

        if (auth.currentUser == null) {
            // Redirect to login, but assume logged in
            finish()
            return
        }

        dbRef = FirebaseDatabase.getInstance().reference.child("users").child(auth.currentUser!!.uid).child("ventas")

        tvVacio = findViewById(R.id.tvVacio)
        rvVentas = findViewById(R.id.rvVentas)
        fabAgregar = findViewById(R.id.fabAgregar)

        adapter = VentaAdapter(mutableListOf(), this)
        rvVentas.layoutManager = LinearLayoutManager(this)
        rvVentas.adapter = adapter

        fabAgregar.setOnClickListener {
            val intent = Intent(this, AddVentaActivity::class.java)
            startActivity(intent)
        }

        escucharCambios()
    }

    private fun escucharCambios() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = mutableListOf<Venta>()
                for (hijo in snapshot.children) {
                    val venta = hijo.getValue(Venta::class.java)
                    if (venta != null) lista.add(venta)
                }
                adapter.setData(lista)
                tvVacio.visibility = if (lista.isEmpty()) TextView.VISIBLE else TextView.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@VentasActivity, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onEditar(venta: Venta) {
        val intent = Intent(this, AddVentaActivity::class.java)
        intent.putExtra(AddVentaActivity.EXTRA_ID, venta.id)
        intent.putExtra(AddVentaActivity.EXTRA_VENTA, venta)
        startActivity(intent)
    }

    override fun onEliminar(venta: Venta) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Eliminar venta a ${venta.clienteNombre}?")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                // Add back stock
                dbRefProductos.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val productMap = mutableMapOf<String, Producto>()
                        for (child in snapshot.children) {
                            val p = child.getValue(Producto::class.java)
                            if (p != null && p.id != null) productMap[p.id!!] = p
                        }
                        venta.productos?.forEach { item ->
                            val p = productMap[item.productoId ?: ""] ?: return@forEach
                            p.stock = (p.stock ?: 0) + (item.cantidad ?: 0)
                            dbRefProductos.child(p.id!!).setValue(p)
                        }
                        // Delete venta
                        venta.id?.let { dbRef.child(it).removeValue() }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}