package com.agrosync.agrosyncapp.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.databinding.FragmentLoginBinding
import com.agrosync.agrosyncapp.ui.LoginUiState
import com.agrosync.agrosyncapp.viewModel.AuthViewModel
import com.agrosync.agrosyncapp.ui.activity.MainActivity
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
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finish()
                        }
                        LoginUiState.ERROR -> Log.d(TAG, "ERROR")
                    }
                }
            }
        }

        binding?.btnSignIn?.setOnClickListener {
            val email: String = binding?.etEmail?.text.toString()
            val password: String = binding?.etPassword?.text.toString()

            binding!!.loading.visibility = View.VISIBLE

            if(email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    vm.login(email, password)
//                    navController.navigate(R.id.action_loginFragment_to_homeFragment)
                }
            } else {
                Toast.makeText(requireContext(), "Por favor, preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
            }
        }

        binding?.tvRegisterHere?.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding?.tvRecovery?.setOnClickListener {
            // Navega para o fragmento de recuperação de senha
            navController.navigate(R.id.action_loginFragment_to_passwordRecoveryFragment)
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