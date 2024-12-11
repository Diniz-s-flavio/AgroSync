package com.agrosync.agrosyncapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.databinding.ActivityMainBinding
import com.agrosync.agrosyncapp.ui.fragment.ProfileEditFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        drawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.navigationView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.resourceCreateFragment, R.id.resourceCreateFragment), drawerLayout
        )

        NavigationUI.setupWithNavController(navigationView, navController)

        val headerView = navigationView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.nomeUser)

        navigationView.menu.findItem(R.id.nav_profile)?.setOnMenuItemClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            navController.navigate(R.id.profileEditFragment)
            true
        }

        fetchUserName(userNameTextView)

        navigationView.menu.findItem(R.id.nav_logout)?.setOnMenuItemClickListener {
            logout()
            true
        }

        binding.btnMenuHamburger.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        setupBottomNavigation()
    }

    fun updateDrawerUsername() {
        val navigationView: NavigationView = binding.navigationView
        val headerView = navigationView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.nomeUser)
        fetchUserName(userNameTextView)
    }

    private fun fetchUserName(userNameTextView: TextView) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("user")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val firstName = document.getString("firstName")
                        val lastName = document.getString("lastName")
                        val fullName = if (!firstName.isNullOrEmpty() && !lastName.isNullOrEmpty()) {
                            "$firstName $lastName"
                        } else if (!firstName.isNullOrEmpty()) {
                            firstName
                        } else {
                            "Usuário"
                        }
                        userNameTextView.text = fullName
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("MainActivity", "Erro ao buscar nome do usuário", e)
                    userNameTextView.text = "Usuário"
                }
        }
    }

    private fun logout() {
        auth.signOut()

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    navController.navigate(R.id.financialFragment)
                    true
                }
                R.id.page_2 -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.page_3 -> {
                    navController.navigate(R.id.inventoryFragment)
                    true
                }
                else -> false
            }
        }
    }
}
