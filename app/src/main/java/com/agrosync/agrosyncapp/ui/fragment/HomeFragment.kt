package com.agrosync.agrosyncapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var btnMenuHamburger: ImageButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        drawerLayout = view.findViewById(R.id.drawerLayout)
        navigationView = view.findViewById(R.id.navigationView)
        btnMenuHamburger = view.findViewById(R.id.btnMenuHamburger)

        // Configurar ações do menu
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    // Ação para tela de perfil
                    Toast.makeText(context, "Perfil selecionado", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_configs -> {
                    // Ação para tela de configurações
                    Toast.makeText(context, "Configurações selecionadas", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_leave -> {
                    // Ação para sair
                    Toast.makeText(context, "Saindo...", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        // Abrir e fechar menu
        btnMenuHamburger.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottom_navigation)

        binding?.bottomNavigation?.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    // Troque para o fragmento relacionado ao Item 1
//                    navigateToFragment(Item1Fragment())
                    true
                }

                R.id.page_2 -> {
                    // Troque para o fragmento relacionado ao Item 2
//                    navigateToFragment(Item2Fragment())
                    true
                }

                else -> false
            }
        }

    }
}