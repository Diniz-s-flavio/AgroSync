package com.agrosync.agrosyncapp.ui.activity.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private var binding: ActivityAuthBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}