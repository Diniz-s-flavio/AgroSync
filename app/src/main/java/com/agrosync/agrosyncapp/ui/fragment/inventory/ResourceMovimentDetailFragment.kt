package com.agrosync.agrosyncapp.ui.fragment.inventory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.data.model.Finance
import com.agrosync.agrosyncapp.data.model.ResourceMovement
import com.agrosync.agrosyncapp.databinding.FragmentFinanceDetailBinding
import com.agrosync.agrosyncapp.databinding.FragmentResourceMovimentDetailBinding
import com.agrosync.agrosyncapp.viewModel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class ResourceMovimentDetailFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.Factory
    }
    private lateinit var _binding: FragmentResourceMovimentDetailBinding
    private  val binding get() = _binding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResourceMovimentDetailBinding.inflate(inflater, container, false)

        val resourceMoviment = arguments?.getSerializable("resourceMoviment") as? ResourceMovement

        resourceMoviment?.let { it ->
            binding.edtDateDisplay.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it.movementDate)
            binding.tvOperationLabel.text = it.operation.displayName
            binding.tvAmountOldValueLabel.text = it.oldResourceAmount.toInt().toString()
            binding.tvAmountMovimentValueLabel.text = it.movementAmount.toInt().toString()
            binding.tvAmountNewValueLabel.text = it.newResourceAmount.toInt().toString()
            binding.tvOperationLabel.text = it.operation.displayName
            binding.tvValueLabel.text = "R$${String.format("%.2f", it.value)}"
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController =  view.findNavController()

        binding.btnReturn.setOnClickListener{
            val resourceMoviment = arguments?.getSerializable("resourceMoviment") as? ResourceMovement
            val bundle = Bundle()
            bundle.putString("resourceId", resourceMoviment?.resourceId)
            navController.navigate(R.id.action_resourceMovimentDetailFragment_to_resourceMovimentFragment,bundle)
        }
    }
}