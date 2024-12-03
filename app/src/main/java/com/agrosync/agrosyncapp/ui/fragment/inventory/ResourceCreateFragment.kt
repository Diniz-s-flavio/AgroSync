package com.agrosync.agrosyncapp.ui.fragment.inventory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.agrosync.agrosyncapp.data.model.ResourceCategory
import com.agrosync.agrosyncapp.databinding.FragmentResourceCreateBinding
import com.agrosync.agrosyncapp.ui.LoginUiState
import com.agrosync.agrosyncapp.ui.activity.MainActivity
import com.agrosync.agrosyncapp.viewModel.MainViewModel
import kotlinx.coroutines.launch

class ResourceCreateFragment : Fragment() {
    private var _binding: FragmentResourceCreateBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val vm: MainViewModel by viewModels { MainViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResourceCreateBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.authUiState.collect {state ->
                    when(state) {
                        LoginUiState.LOADING -> Log.d(TAG, "LOADING")
                        LoginUiState.SUCCESS -> {
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finish()
                        }
                        LoginUiState.ERROR -> Log.d(TAG, "ERROR")
                    }
                }
            }
        }

        val categories = listOf(
            ResourceCategory("1", "Eletrônicos"),
            ResourceCategory("2", "Roupas"),
            ResourceCategory("3", "Alimentos"),
            ResourceCategory("4", "Livros")
        )

        val categoryNames = categories.map { it.name }
        Log.d("ResourceCreateFragment", "Categorias: $categoryNames")

        // Configurar o ArrayAdapter
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categoryNames
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Atribuir o adapter ao Spinner
        binding.spinnerCategories.adapter = adapter

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}