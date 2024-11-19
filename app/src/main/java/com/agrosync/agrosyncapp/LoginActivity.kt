package com.agrosync.agrosyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.agrosync.agrosyncapp.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.btnSingIn.setOnClickListener {
            Snackbar.make(binding.root, "Botão Login clicado",Snackbar.LENGTH_SHORT).show()
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Snackbar.make(binding.root, "Por favor preencha todos os campos",
                    Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            signIn(email, password)
        }
        binding.tvRegisterHere.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun signIn(email:String, password:String) {
        Log.d("LoginActivity", "Tentativa de login")
        binding.loaging.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(this) {task ->
                    binding.loaging.visibility = View.GONE
                    if (task.isSuccessful){
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }else {
                        Snackbar.make(binding.root, "Falha ao entrar:" +
                                "${task.exception?.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }
    }

    private fun register(email:String, password:String) {
        Log.d("LoginActivity", "Tentativa de registro")
        binding.loaging.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password).
        addOnCompleteListener(this) {task ->
            binding.loaging.visibility = View.GONE
            if (task.isSuccessful){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else {
                Snackbar.make(binding.root, "Falha ao registrar:" +
                        "${task.exception?.message}", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}