package com.agrosync.agrosyncapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.databinding.ActivityMainBinding
import com.agrosync.agrosyncapp.ui.fragment.inventory.ResourceCreateFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // Carregar o fragmento na Activity
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ResourceCreateFragment())
                .commit()
        }
    }

}