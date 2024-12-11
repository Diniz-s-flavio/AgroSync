package com.agrosync.agrosyncapp.ui.fragment.financial

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
import com.agrosync.agrosyncapp.databinding.FragmentFinanceCreateBinding
import com.agrosync.agrosyncapp.databinding.FragmentFinanceDetailBinding
import com.agrosync.agrosyncapp.databinding.FragmentResourceDetailBinding
import com.agrosync.agrosyncapp.viewModel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class FinanceDetailFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.Factory
    }
    private lateinit var _binding: FragmentFinanceDetailBinding
    private  val binding get() = _binding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinanceDetailBinding.inflate(inflater, container, false)

        val finance = arguments?.getSerializable("finance") as? Finance

        finance?.let { it ->
            binding.tvFinanceDetailTitle.text = it.title
            binding.tvValorLabel.text = "R$${String.format("%.2f", it.value)}"
            binding.tvDateLabel.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it.date)
            binding.tvOperationLabel.text = it.operation.displayName
            binding.tvDescricaoLabel.text = it.description

            Log.d("FinanceDetailFragment", "isFromResource: ${it.isFromResource}")
            if (it.isFromResource){
                binding.btnGoToResource.visibility = View.VISIBLE
            }

            it.resource.let { itResource ->
                mainViewModel.refResource = itResource
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController =  view.findNavController()

        binding.btnGoToResource.setOnClickListener{
            navController.navigate(R.id.action_financialDetailFragment_to_resourceDetailFragment)
        }
    }
}
