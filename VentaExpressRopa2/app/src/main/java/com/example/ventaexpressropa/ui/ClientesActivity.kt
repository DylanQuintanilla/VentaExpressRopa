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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.ventaexpressropa.R
import com.google.firebase.database.ValueEventListener
import com.example.ventaexpressropa.adapters.ClientAdapter
import com.example.ventaexpressropa.models.Cliente
class ClientesActivity : AppCompatActivity(), ClientAdapter.OnClientAction {

    private lateinit var dbRef: DatabaseReference
    private lateinit var adapter: ClientAdapter

    private lateinit var tvVacio: TextView
    private lateinit var rvClientes: RecyclerView
    private lateinit var fabAgregar: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_clientes)

        dbRef = FirebaseDatabase.getInstance().reference.child("clientes")

        tvVacio = findViewById(R.id.tvVacio)
        rvClientes = findViewById(R.id.rvClientes)
        fabAgregar = findViewById(R.id.fabAgregar)

        adapter = ClientAdapter(mutableListOf(), this)
        rvClientes.layoutManager = LinearLayoutManager(this)
        rvClientes.adapter = adapter

        fabAgregar.setOnClickListener {
            val intent = Intent(this, AddClienteActivity::class.java)
            startActivity(intent)
        }

        escucharCambios()
    }

    private fun escucharCambios() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = mutableListOf<Cliente>()
                for (hijo in snapshot.children) {
                    val cliente = hijo.getValue(Cliente::class.java)
                    if (cliente != null) lista.add(cliente)
                }
                adapter.setData(lista)
                tvVacio.visibility = if (lista.isEmpty()) TextView.VISIBLE else TextView.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ClientesActivity, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onEditar(cliente: Cliente) {
        val intent = Intent(this, AddClienteActivity::class.java)
        intent.putExtra(AddClienteActivity.EXTRA_ID, cliente.id)
        intent.putExtra(AddClienteActivity.EXTRA_NOMBRE, cliente.nombre)
        intent.putExtra(AddClienteActivity.EXTRA_CORREO, cliente.correo)
        intent.putExtra(AddClienteActivity.EXTRA_TELEFONO, cliente.telefono)
        startActivity(intent)
    }

    override fun onEliminar(cliente: Cliente) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Eliminar ${cliente.nombre}?")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                cliente.id?.let { dbRef.child(it).removeValue() }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

}