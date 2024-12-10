package com.agrosync.agrosyncapp.ui.fragment

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.agrosync.agrosyncapp.data.model.Operation
import com.agrosync.agrosyncapp.data.repository.FinanceRepository
import com.agrosync.agrosyncapp.databinding.FragmentHomeBinding
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

    private val months = arrayOf(
        "Janeiro", "Fevereiro", "Março", "Abril",
        "Maio", "Junho", "Julho", "Agosto",
        "Setembro", "Outubro", "Novembro", "Dezembro"
    )


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
        setupMonthSpinner()
    }

    private fun setupMonthSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            months
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerPeriodo.adapter = adapter

        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        binding.spinnerPeriodo.setSelection(currentMonth)

        binding.spinnerPeriodo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadFinancialData(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
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

    private fun loadFinancialData(selectedMonthPosition: Int) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            financeRepository.findByUserId(
                userId,
                onSuccess = { finances ->
                    // Calculate finances for the selected month
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.MONTH, selectedMonthPosition)
                    val selectedMonth = selectedMonthPosition
                    val selectedYear = calendar.get(Calendar.YEAR)

                    val monthlyFinances = finances.filter { finance ->
                        val financeCalendar = Calendar.getInstance().apply {
                            time = finance.date
                        }
                        financeCalendar.get(Calendar.MONTH) == selectedMonth &&
                                financeCalendar.get(Calendar.YEAR) == selectedYear
                    }

                    val totalEntry = monthlyFinances
                        .filter { it.operation == Operation.ENTRY }
                        .sumOf { it.value }

                    val totalWithdrawal = monthlyFinances
                        .filter { it.operation == Operation.WITHDRAWAL }
                        .sumOf { it.value }

                    val total = totalEntry + totalWithdrawal

                    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

                    binding.tvValorTotal.text = currencyFormatter.format(total)
                    binding.tvValorEntrada.text = currencyFormatter.format(totalEntry)
                    binding.tvValorGasts.text = currencyFormatter.format(totalWithdrawal)
                },
                onFailure = { e ->
                    Log.e("HomeFragment", "Erro ao carregar finanças", e)
                    binding.tvValorTotal.text = "R$ 0,00"
                    binding.tvValorEntrada.text = "R$ 0,00"
                    binding.tvValorGasts.text = "R$ 0,00"
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
