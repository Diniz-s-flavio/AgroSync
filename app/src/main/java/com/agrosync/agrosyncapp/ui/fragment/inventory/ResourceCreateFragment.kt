package com.agrosync.agrosyncapp.ui.fragment.inventory

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.agrosync.agrosyncapp.databinding.FragmentResourceCreateBinding
import com.agrosync.agrosyncapp.ui.LoginUiState
import com.agrosync.agrosyncapp.ui.activity.MainActivity
import com.agrosync.agrosyncapp.viewModel.MainViewModel
import kotlinx.coroutines.launch

class ResourceCreateFragment : Fragment() {
    private var binding: FragmentResourceCreateBinding? = null
    private lateinit var navController: NavController

    private val vm: MainViewModel by viewModels { MainViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResourceCreateBinding.inflate(inflater, container, false)
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

        val options = listOf("Opção 1", "Opção 2", "Opção 3", "Opção 4")


        // Configurar o adaptador
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, options)
        binding?.spinnerCategoria?.setAdapter(adapter)

        // Configurar comportamento de clique
        binding?.spinnerCategoria?.setOnItemClickListener { _, _, position, _ ->
            val selectedOption = options[position]
            // Ação ao selecionar um item
            println("Selecionado: $selectedOption")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}