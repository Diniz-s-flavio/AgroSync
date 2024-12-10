package com.agrosync.agrosyncapp.ui.fragment.financial

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.Finance
import com.agrosync.agrosyncapp.data.model.Operation
import com.agrosync.agrosyncapp.data.repository.FinanceRepository
import com.agrosync.agrosyncapp.databinding.FragmentFinancialBinding
import com.agrosync.agrosyncapp.databinding.FragmentHomeBinding
import com.agrosync.agrosyncapp.ui.adapter.FinanceAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale


class FinancialFragment : Fragment() {

    private var _binding: FragmentFinancialBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var financeRepository: FinanceRepository
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var financeAdapter: FinanceAdapter

    private val months = arrayOf(
        "Janeiro", "Fevereiro", "Março", "Abril",
        "Maio", "Junho", "Julho", "Agosto",
        "Setembro", "Outubro", "Novembro", "Dezembro"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFinancialBinding.inflate(inflater, container, false)

        financeRepository = FinanceRepository()
        firebaseAuth = FirebaseAuth.getInstance()

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        setupRecyclerView()

        binding?.floatingActionButton3?.setOnClickListener {
            navController.navigate(R.id.action_financialFragment_to_finance_create_fragment)
        }

//        fetchFinances()
        setupMonthSpinner()
    }

    private fun setupRecyclerView() {
        financeAdapter = FinanceAdapter(emptyList()) { finance ->
            val bundle = Bundle()
            bundle.putSerializable("finance", finance)

            navController.navigate(R.id.action_financialFragment_to_financialDetailFragment, bundle)
        }

        binding?.financiaRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = financeAdapter
        }
    }

    private fun setupMonthSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
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

    private fun fetchFinances() {
        val userUid = firebaseAuth.currentUser?.uid
        userUid?.let { uid ->
            lifecycleScope.launch {
                financeRepository.findByUserId(
                    uid,
                    onSuccess = { finances ->
                        updateFinanceList(finances)
                    },
                    onFailure = { exception ->
                        Toast.makeText(
                            requireContext(),
                            "Erro ao carregar finanças: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        } ?: run {
            Toast.makeText(
                requireContext(),
                "Usuário não autenticado",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadFinancialData(selectedMonthPosition: Int) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            financeRepository.findByUserId(
                userId,
                onSuccess = { finances ->
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

                    updateFinanceList(monthlyFinances)

                },
                onFailure = { e ->
                    Log.e("FinancialFragment", "Erro ao carregar finanças", e)
                }
            )
        }
    }

    private fun updateFinanceList(finances: List<Finance>) {
        financeAdapter = FinanceAdapter(finances) { finance ->
            val bundle = Bundle()
            bundle.putSerializable("finance", finance)

            navController.navigate(R.id.action_financialFragment_to_financialDetailFragment, bundle)
        }
        binding?.financiaRecyclerView?.adapter = financeAdapter
        financeAdapter.notifyDataSetChanged()

        if (finances.isEmpty()) {
            binding?.financiaRecyclerView?.visibility = View.GONE
        } else {
            binding?.financiaRecyclerView?.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
