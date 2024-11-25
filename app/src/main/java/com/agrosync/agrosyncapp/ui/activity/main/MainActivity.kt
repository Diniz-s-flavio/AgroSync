package com.agrosync.agrosyncapp.ui.activity.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.agrosync.agrosyncapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null){
            val displayName =currentUser.displayName
            val userName = if(displayName.isNullOrEmpty()){
                currentUser.email?: "Usuario sem nome"
            }else {
                displayName + "\nSeja bem-vindo"
            }
            binding.tvUsuario.text = userName
        }else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.deslogar.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}