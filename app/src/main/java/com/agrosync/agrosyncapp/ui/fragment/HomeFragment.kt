package com.agrosync.agrosyncapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var btnMenuHamburger: ImageButton

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView
        btnMenuHamburger = binding.btnMenuHamburger

        setupNavigationDrawer()

        setupBottomNavigation()

        loadUserName()
    }

    private fun setupNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    Toast.makeText(context, "Perfil selecionado", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_configs -> {
                    Toast.makeText(context, "Configurações selecionadas", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_leave -> {
                    Toast.makeText(context, "Saindo...", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        btnMenuHamburger.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    Toast.makeText(context, "Página 1 selecionada", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.page_2 -> {
                    Toast.makeText(context, "Página 2 selecionada", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadUserName() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val userDocRef = firestore.collection("user").document(uid)

            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val firstName = document.getString("firstName") ?: "Usuário"
                        binding.tvWelcome.text = "Bem-vindo, $firstName!"
                    } else {
                        binding.tvWelcome.text = "Nome não encontrado."
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("HomeFragment", "Erro ao buscar o nome do usuário", e)
                    binding.tvWelcome.text = "Erro ao carregar o nome."
                }
        } else {
            binding.tvWelcome.text = "Usuário não encontrado."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
