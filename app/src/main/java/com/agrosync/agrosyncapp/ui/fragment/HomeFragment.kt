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

        loadUserName()
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
