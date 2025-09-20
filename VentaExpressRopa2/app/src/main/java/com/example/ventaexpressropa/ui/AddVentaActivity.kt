package com.example.ventaexpressropa.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.ventaexpressropa.R
import com.example.ventaexpressropa.adapters.VentaProductoAdapter
import com.example.ventaexpressropa.models.Cliente
import com.example.ventaexpressropa.models.Producto
import com.example.ventaexpressropa.models.ProductoVenta
import com.example.ventaexpressropa.models.Venta
class AddVentaActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val dbRefVentas = FirebaseDatabase.getInstance().reference.child("users").child(auth.currentUser?.uid ?: "").child("ventas")
    private val dbRefClientes = FirebaseDatabase.getInstance().reference.child("clientes")
    private val dbRefProductos = FirebaseDatabase.getInstance().reference.child("productos")
    private var editId: String? = null
    private var oldVenta: Venta? = null

    private lateinit var spCliente: Spinner
    private lateinit var rvItems: RecyclerView
    private lateinit var btnAddItem: Button
    private lateinit var tvTotal: TextView
    private lateinit var btnGuardar: Button

    private var listClientes: MutableList<Cliente> = mutableListOf()
    private var listProductos: MutableList<Producto> = mutableListOf()
    private var selectedProductos: MutableList<ProductoVenta> = mutableListOf()
    private lateinit var adapterItems: VentaProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_venta)
        if (auth.currentUser == null) {
            // Redirect to login, but assume logged in
            finish()
            return
        }

        spCliente = findViewById(R.id.spCliente)
        rvItems = findViewById(R.id.rvItems)
        btnAddItem = findViewById(R.id.btnAddItem)
        tvTotal = findViewById(R.id.tvTotal)
        btnGuardar = findViewById(R.id.btnGuardar)

        adapterItems = VentaProductoAdapter(selectedProductos)
        rvItems.layoutManager = LinearLayoutManager(this)
        rvItems.adapter = adapterItems

        editId = intent.getStringExtra(EXTRA_ID)
        oldVenta = intent.getParcelableExtra(EXTRA_VENTA)

        btnAddItem.setOnClickListener { showAddItemDialog() }
        btnGuardar.setOnClickListener { guardar() }

        fetchClientes()
        fetchProductos()

        if (oldVenta != null) {
            selectedProductos.addAll(oldVenta?.productos ?: emptyList())
            adapterItems.setData(selectedProductos)
            updateTotal()
        }
    }

    private fun fetchClientes() {
        dbRefClientes.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listClientes.clear()
                for (child in snapshot.children) {
                    val cliente = child.getValue(Cliente::class.java)
                    if (cliente != null) listClientes.add(cliente)
                }
                val nombres = listClientes.map { it.nombre ?: "" }
                val adapter = ArrayAdapter(this@AddVentaActivity, android.R.layout.simple_spinner_item, nombres)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spCliente.adapter = adapter

                if (oldVenta != null) {
                    val pos = listClientes.indexOfFirst { it.id == oldVenta?.clienteId }
                    if (pos >= 0) spCliente.setSelection(pos)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddVentaActivity, "Error al cargar clientes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchProductos() {
        dbRefProductos.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listProductos.clear()
                for (child in snapshot.children) {
                    val producto = child.getValue(Producto::class.java)
                    if (producto != null) listProductos.add(producto)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddVentaActivity, "Error al cargar productos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showAddItemDialog() {
        val dialogView = LinearLayout(this)
        dialogView.orientation = LinearLayout.VERTICAL
        dialogView.setPadding(20, 20, 20, 20)

        val spProducto = Spinner(this)
        val nombres = listProductos.map { it.nombre ?: "" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spProducto.adapter = adapter
        dialogView.addView(spProducto)

        val etCantidad = EditText(this)
        etCantidad.hint = "Cantidad"
        etCantidad.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        dialogView.addView(etCantidad)

        AlertDialog.Builder(this)
            .setTitle("Agregar Producto")
            .setView(dialogView)
            .setPositiveButton("Agregar") { _, _ ->
                val pos = spProducto.selectedItemPosition
                if (pos >= 0) {
                    val producto = listProductos[pos]
                    val cantidad = etCantidad.text.toString().toIntOrNull() ?: 0
                    if (cantidad > 0) {
                        val item = ProductoVenta(producto.id, producto.nombre, cantidad, producto.precio)
                        selectedProductos.add(item)
                        adapterItems.addItem(item)
                        updateTotal()
                    } else {
                        Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun updateTotal() {
        val total = selectedProductos.sumOf { (it.cantidad ?: 0) * (it.precioUnitario ?: 0.0) }
        tvTotal.text = "Total: $$total"
    }

    private fun guardar() {
        val posCliente = spCliente.selectedItemPosition
        if (posCliente < 0 || selectedProductos.isEmpty()) {
            Toast.makeText(this, "Selecciona cliente y agrega productos", Toast.LENGTH_SHORT).show()
            return
        }
        val cliente = listClientes[posCliente]
        val total = selectedProductos.sumOf { (it.cantidad ?: 0) * (it.precioUnitario ?: 0.0) }

        // Fetch current products for stock check and update
        dbRefProductos.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productMap = mutableMapOf<String, Producto>()
                for (child in snapshot.children) {
                    val p = child.getValue(Producto::class.java)
                    if (p != null && p.id != null) productMap[p.id!!] = p
                }

                // If edit, add back old quantities
                if (editId != null) {
                    oldVenta?.productos?.forEach { oldItem ->
                        val p = productMap[oldItem.productoId ?: ""] ?: return@forEach
                        p.stock = (p.stock ?: 0) + (oldItem.cantidad ?: 0)
                        dbRefProductos.child(p.id!!).setValue(p)
                    }
                }

                // Check stock for new items
                var allOk = true
                selectedProductos.forEach { item ->
                    val p = productMap[item.productoId ?: ""] ?: return@forEach
                    if ((p.stock ?: 0) < (item.cantidad ?: 0)) {
                        allOk = false
                        Toast.makeText(this@AddVentaActivity, "Stock insuficiente para ${p.nombre}", Toast.LENGTH_SHORT).show()
                    }
                }

                if (allOk) {
                    // Subtract new quantities
                    selectedProductos.forEach { item ->
                        val p = productMap[item.productoId ?: ""] ?: return@forEach
                        p.stock = (p.stock ?: 0) - (item.cantidad ?: 0)
                        dbRefProductos.child(p.id!!).setValue(p)
                    }

                    // Save venta
                    val id = editId ?: dbRefVentas.push().key ?: return
                    val venta = Venta(id, cliente.id, cliente.nombre, selectedProductos, total, System.currentTimeMillis())
                    dbRefVentas.child(id).setValue(venta)
                        .addOnSuccessListener { finish() }
                        .addOnFailureListener { Toast.makeText(this@AddVentaActivity, "Error al guardar", Toast.LENGTH_SHORT).show() }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddVentaActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_VENTA = "extra_venta"
    }
}