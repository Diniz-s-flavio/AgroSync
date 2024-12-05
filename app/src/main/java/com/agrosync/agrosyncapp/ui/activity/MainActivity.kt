package com.agrosync.agrosyncapp.ui.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.databinding.ActivityMainBinding
import com.agrosync.agrosyncapp.ui.fragment.HomeFragment
import com.agrosync.agrosyncapp.ui.fragment.inventory.ResourceCreateFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        setupBottomNavigation()
        setupNavigationDrawer()
    }

    private fun setupNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    loadFragment(HomeFragment(), "Perfil")
                    true
                }
                R.id.nav_configs -> {
                    loadFragment(HomeFragment(), "Configurações")
                    true
                }
                R.id.nav_leave -> {
                    true
                }
                else -> false
            }
        }

        binding.btnMenuHamburger.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    loadFragment(HomeFragment(), "Home")
                    true
                }
                R.id.page_2 -> {
                    loadFragment(HomeFragment(), "Perfil")
                    true
                }
                R.id.page_3 -> {
                    loadFragment(ResourceCreateFragment(), "Configurações")
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
        supportActionBar?.title = title
    }

}