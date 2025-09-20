package com.example.ventaexpressropa.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ventaexpressropa.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var btnProductos: Button
    private lateinit var btnClientes: Button
    private lateinit var btnVentas: Button
    private lateinit var btnLogout: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        btnLogout = findViewById(R.id.btnLogout)

        btnProductos = findViewById<Button>(R.id.btnProductos)
        btnClientes = findViewById<Button>(R.id.btnClientes)
        btnVentas = findViewById<Button>(R.id.btnVentas)

        btnProductos.setOnClickListener {
            // Crea el nuevo Intent y guárdalo en una variable (ej. `val nuevoIntent`)
            val nuevoIntent = Intent(this, ProductosActivity::class.java)

            // Pasa el nuevo Intent a startActivity
            startActivity(nuevoIntent)
        }

        btnClientes.setOnClickListener {
            // Crea el nuevo Intent y guárdalo en una variable (ej. `val nuevoIntent`)
            val nuevoIntent = Intent(this, ClientesActivity::class.java)
            // Pasa el nuevo Intent a startActivity
            startActivity(nuevoIntent)
        }

        btnVentas.setOnClickListener {
            // Crea el nuevo Intent y guárdalo en una variable (ej. `val nuevoIntent`)
            val nuevoIntent = Intent(this, VentasActivity::class.java)
            // Pasa el nuevo Intent a startActivity
            startActivity(nuevoIntent)
        }

        btnLogout.setOnClickListener {
            logoutUser()
        }

    }

    private fun logoutUser() {
        auth.signOut()
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}