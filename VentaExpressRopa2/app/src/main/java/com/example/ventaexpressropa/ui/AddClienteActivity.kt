package com.example.ventaexpressropa.ui

import com.example.ventaexpressropa.models.Cliente
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

class AddClienteActivity : AppCompatActivity() {

    private val dbRef = FirebaseDatabase.getInstance().reference.child("clientes")
    private var editId: String? = null

    private lateinit var etNombre: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etTelefono: EditText
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_cliente)

        etNombre = findViewById(R.id.etNombre)
        etCorreo = findViewById(R.id.etCorreo)
        etTelefono = findViewById(R.id.etTelefono)
        btnGuardar = findViewById(R.id.btnGuardar)

        editId = intent.getStringExtra(EXTRA_ID)
        etNombre.setText(intent.getStringExtra(EXTRA_NOMBRE))
        etCorreo.setText(intent.getStringExtra(EXTRA_CORREO))
        etTelefono.setText(intent.getStringExtra(EXTRA_TELEFONO))

        btnGuardar.setOnClickListener {
            guardar()
        }
    }

    private fun guardar() {
        val nombre = etNombre.text?.toString()?.trim().orEmpty()
        val correo = etCorreo.text?.toString()?.trim().orEmpty()
        val telefono = etTelefono.text?.toString()?.trim().orEmpty()

        if (nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (editId == null) {
            val id = dbRef.push().key ?: return
            val cliente = Cliente(id, nombre, correo, telefono)
            dbRef.child(id).setValue(cliente)
                .addOnSuccessListener { finish() }
                .addOnFailureListener { Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show() }
        } else {
            val cliente = Cliente(editId, nombre, correo, telefono)
            dbRef.child(editId!!).setValue(cliente)
                .addOnSuccessListener { finish() }
                .addOnFailureListener { Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show() }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NOMBRE = "extra_nombre"
        const val EXTRA_CORREO = "extra_correo"
        const val EXTRA_TELEFONO = "extra_telefono"
    }
}