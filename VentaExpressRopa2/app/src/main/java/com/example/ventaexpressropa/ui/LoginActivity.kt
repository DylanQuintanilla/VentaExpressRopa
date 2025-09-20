package com.example.ventaexpressropa.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ventaexpressropa.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import java.util.Arrays


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var btnLoginGithub: Button
    private lateinit var btnLoginFacebook: Button

    var callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Verificar si ya hay un usuario autenticado
        if (auth.currentUser != null) {
            goToMainActivity()
            return
        }

        // Vincular vistas
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        btnLoginGithub = findViewById(R.id.btn_login_github)
        btnLoginFacebook = findViewById(R.id.btn_login_facebook)

        // Configurar listeners
        btnLogin.setOnClickListener { loginUser() }
        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLoginGithub.setOnClickListener {
            loginWithGitHub()
        }

        btnLoginFacebook.setOnClickListener {
            login_facebook()
        }

    }

    fun login_facebook(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    //Log.d(TAG, "facebook:onSuccess:$loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException) {
                    //Log.d(TAG, "facebook:onError", error)
                }
            },
        )
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    var intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(applicationContext,"Error Al Iniciar", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    goToMainActivity()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginWithGitHub() {
        val provider = OAuthProvider.newBuilder("github.com")

        // Verificar si hay un resultado pendiente (por ejemplo, si el flujo fue interrumpido)
        val pendingResultTask = auth.pendingAuthResult
        if (pendingResultTask != null) {
            // Completar el inicio de sesión con el resultado pendiente
            pendingResultTask
                .addOnSuccessListener { authResult ->
                    val user = authResult.user
                    Toast.makeText(this, "Inicio de sesión con GitHub exitoso como ${user?.displayName ?: user?.email}", Toast.LENGTH_SHORT).show()
                    goToMainActivity()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error al completar login con GitHub: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Iniciar el flujo de iniseio de sesión si no hay resultado pendiente
            auth.startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener { authResult ->
                    val user = authResult.user
                    Toast.makeText(this, "Inicio de sesión con GitHub exitoso como ${user?.displayName ?: user?.email}", Toast.LENGTH_SHORT).show()
                    goToMainActivity()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error en login con GitHub: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }



    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            goToMainActivity()
        }
    }
}