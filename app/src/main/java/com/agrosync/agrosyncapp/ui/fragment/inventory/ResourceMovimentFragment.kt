package com.agrosync.agrosyncapp.ui.fragment.inventory

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.ResourceMovement
import com.agrosync.agrosyncapp.data.repository.ResourceMovementRepository
import com.agrosync.agrosyncapp.databinding.FragmentResourceMovimentBinding
import com.agrosync.agrosyncapp.ui.adapter.ResourceMovimentAdapter
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class ResourceMovimentFragment : Fragment() {

    private var _binding: FragmentResourceMovimentBinding? = null
    private val binding get() = _binding!!
    private lateinit var resourceMovimentAdapter: ResourceMovimentAdapter
    private lateinit var resourceMovimentRepository: ResourceMovementRepository
    private lateinit var firebaseAuth: FirebaseAuth

    private val months = arrayOf(
        "Janeiro", "Fevereiro", "Março", "Abril",
        "Maio", "Junho", "Julho", "Agosto",
        "Setembro", "Outubro", "Novembro", "Dezembro"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResourceMovimentBinding.inflate(inflater, container, false)

        resourceMovimentRepository = ResourceMovementRepository()
        firebaseAuth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        setupMonthSpinner()

    }

    private fun setupRecyclerView() {
        resourceMovimentAdapter = ResourceMovimentAdapter(emptyList()) { resourceMoviments ->
            val bundle = Bundle()
            bundle.putSerializable("resourceMoviments", resourceMoviments)
        }

        binding.resourceMovimentRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = resourceMovimentAdapter
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
                loadResourceMovimentData(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun loadResourceMovimentData(selectedMonthPosition: Int) {
        val currentUser = firebaseAuth.currentUser
        val resourceId = arguments?.getString("resourceId")
        if (currentUser != null) {
            val userId = currentUser.uid

            resourceId?.let {
                resourceMovimentRepository.findByResourceId(
                    it,
                    onSuccess = { resourceMoviments ->
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.MONTH, selectedMonthPosition)
                        val selectedMonth = selectedMonthPosition
                        val selectedYear = calendar.get(Calendar.YEAR)

                        val monthlyFinances = resourceMoviments.filter { finance ->
                            val resourceMovimentCalendar = Calendar.getInstance().apply {
                                time = finance.date
                            }
                            resourceMovimentCalendar.get(Calendar.MONTH) == selectedMonth &&
                                    resourceMovimentCalendar.get(Calendar.YEAR) == selectedYear
                        }

                        updateResourceMovimentList(monthlyFinances)

                    },
                    onFailure = { e ->
                        Log.e("FinancialFragment", "Erro ao carregar finanças", e)
                    }
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateResourceMovimentList(resourceMoviments: List<ResourceMovement>) {
        resourceMovimentAdapter = ResourceMovimentAdapter(resourceMoviments) { resourceMoviments ->
//            val bundle = Bundle()
//            bundle.putSerializable("finance", resourceMoviments)
//
//            navController.navigate(R.id.action_financialFragment_to_financialDetailFragment, bundle)
        }
        binding.resourceMovimentRecyclerView.adapter = resourceMovimentAdapter
        resourceMovimentAdapter.notifyDataSetChanged()

        if (resourceMoviments.isEmpty()) {
            binding.resourceMovimentRecyclerView.visibility = View.GONE
        } else {
            binding.resourceMovimentRecyclerView.visibility = View.VISIBLE
        }
    }
}