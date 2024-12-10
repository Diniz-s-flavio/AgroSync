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
import com.agrosync.agrosyncapp.data.model.Operation
import com.agrosync.agrosyncapp.data.repository.FinanceRepository
import com.agrosync.agrosyncapp.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var financeRepository: FinanceRepository

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
        financeRepository = FinanceRepository()

        loadUserName()
        loadFinancialData()
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

    private fun loadFinancialData() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            financeRepository.findByUserId(
                userId,
                onSuccess = { finances ->
                    // Calculate finances for the current month
                    val calendar = Calendar.getInstance()
                    val currentMonth = calendar.get(Calendar.MONTH)
                    val currentYear = calendar.get(Calendar.YEAR)

                    val monthlyFinances = finances.filter { finance ->
                        val financeCalendar = Calendar.getInstance().apply {
                            time = finance.date
                        }
                        financeCalendar.get(Calendar.MONTH) == currentMonth &&
                                financeCalendar.get(Calendar.YEAR) == currentYear
                    }

                    val totalEntry = monthlyFinances
                        .filter { it.operation == Operation.ENTRY }
                        .sumOf { it.value }

                    val totalWithdrawal = monthlyFinances
                        .filter { it.operation == Operation.WITHDRAWAL }
                        .sumOf { it.value }

                    val total = totalEntry - totalWithdrawal

                    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

                    binding.tvValorTotal.text = currencyFormatter.format(total)
                    binding.tvValorEntrada.text = currencyFormatter.format(totalEntry)
                    binding.tvValorGasts.text = currencyFormatter.format(totalWithdrawal)
                },
                onFailure = { e ->
                    Log.e("HomeFragment", "Erro ao carregar finanças", e)
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
