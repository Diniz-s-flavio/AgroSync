package com.agrosync.agrosyncapp.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.databinding.FragmentLoginBinding
import com.agrosync.agrosyncapp.ui.activity.auth.AuthViewModel
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    private var binding: FragmentLoginBinding? = null
    private lateinit var navController: NavController

    private val vm: AuthViewModel by viewModels { AuthViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
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
                            startActivity(Intent(requireContext(), HomeActivity::class.java))
                            requireActivity().finish()
                        }
                        LoginUiState.ERROR -> Log.d(TAG, "ERROR")
                    }
                }
            }
        }

        binding?.btnLogin?.setOnClickListener {
            val email: String = binding?.etEmail?.text.toString()
            val password: String = binding?.etPassword?.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    vm.login(email, password)
                }
            } else {
                Toast.makeText(requireContext(), "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        binding?.tvCreateAccount?.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        private const val TAG = "UiState"
    }
}