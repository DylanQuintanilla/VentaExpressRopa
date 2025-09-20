package com.example.ventaexpressropa.ui

import com.example.ventaexpressropa.models.Producto
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ventaexpressropa.R
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class AddProductoActivity : AppCompatActivity() {

    private val dbRef = FirebaseDatabase.getInstance().reference.child("productos")
    private var editId: String? = null

    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etStock: EditText
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_producto)
        etNombre = findViewById(R.id.etNombre)
        etDescripcion = findViewById(R.id.etDescripcion)
        etPrecio = findViewById(R.id.etPrecio)
        etStock = findViewById(R.id.etStock)
        btnGuardar = findViewById(R.id.btnGuardar)

        editId = intent.getStringExtra(EXTRA_ID)
        etNombre.setText(intent.getStringExtra(EXTRA_NOMBRE))
        etDescripcion.setText(intent.getStringExtra(EXTRA_DESCRIPCION))
        etPrecio.setText(intent.getDoubleExtra(EXTRA_PRECIO, 0.0).toString())
        etStock.setText(intent.getIntExtra(EXTRA_STOCK, 0).toString())

        btnGuardar.setOnClickListener { guardar() }
    }

    private fun guardar() {
        val nombre = etNombre.text?.toString()?.trim().orEmpty()
        val descripcion = etDescripcion.text?.toString()?.trim().orEmpty()
        val precio = etPrecio.text?.toString()?.toDoubleOrNull()
        val stock = etStock.text?.toString()?.toIntOrNull()

        if (nombre.isEmpty() || precio == null || stock == null) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (editId == null) {
            val id = dbRef.push().key ?: return
            val producto = Producto(id, nombre, descripcion, precio, stock)
            dbRef.child(id).setValue(producto)
                .addOnSuccessListener { finish() }
                .addOnFailureListener { Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show() }
        } else {
            val producto = Producto(editId, nombre, descripcion, precio, stock)
            dbRef.child(editId!!).setValue(producto)
                .addOnSuccessListener { finish() }
                .addOnFailureListener { Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show() }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NOMBRE = "extra_nombre"
        const val EXTRA_DESCRIPCION = "extra_descripcion"
        const val EXTRA_PRECIO = "extra_precio"
        const val EXTRA_STOCK = "extra_stock"
    }
}