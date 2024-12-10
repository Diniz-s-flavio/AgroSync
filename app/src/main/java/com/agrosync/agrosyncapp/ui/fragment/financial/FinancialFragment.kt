package com.agrosync.agrosyncapp.ui.fragment.financial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.Finance
import com.agrosync.agrosyncapp.data.repository.FinanceRepository
import com.agrosync.agrosyncapp.databinding.FragmentFinancialBinding
import com.agrosync.agrosyncapp.ui.adapter.FinanceAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class FinancialFragment : Fragment() {

    private var binding: FragmentFinancialBinding? = null
    private lateinit var navController: NavController
    private lateinit var financeRepository: FinanceRepository
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var financeAdapter: FinanceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinancialBinding.inflate(inflater, container, false)

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

        fetchFinances()
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
        binding = null
    }
}
