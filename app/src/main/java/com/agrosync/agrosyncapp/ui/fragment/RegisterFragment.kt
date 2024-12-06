package com.agrosync.agrosyncapp.ui.fragment

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
import com.agrosync.agrosyncapp.data.model.User
import com.agrosync.agrosyncapp.data.model.UserRole
import com.agrosync.agrosyncapp.databinding.FragmentRegisterBinding
import com.agrosync.agrosyncapp.ui.LoginUiState
import com.agrosync.agrosyncapp.viewModel.AuthViewModel
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private var binding: FragmentRegisterBinding? = null
    private val vm: AuthViewModel by viewModels { AuthViewModel.Factory }
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
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
                            navController.navigate(R.id.action_registerFragment_to_loginFragment)
                        }
                        LoginUiState.ERROR -> Log.d(TAG, "ERROR")
                    }
                }
            }
        }

        binding?.btnRegister?.setOnClickListener {
            val user = User("",
                binding?.etFirstName?.text.toString(),
                binding?.etLastName?.text.toString(),
                binding?.etEmail?.text.toString(),
                UserRole.OWNER)
            user.password = binding?.etPassword?.text.toString()
            val confirmPassword: String = binding?.etConfirmPassword?.text.toString()
            Log.d(TAG, "UserName: ${user.firstName} ${user.lastName}")

            if(user.firstName.isNotEmpty() && user.lastName.isNotEmpty() && user.email.isNotEmpty()
                && user.password.isNotEmpty() && user.password.equals(confirmPassword)) {
                Log.d(TAG, "User: $user")
                lifecycleScope.launch {
                    vm.register(user)
                }

            }else {
                Toast.makeText(requireContext(),
                    "Por favor, preencha todos os campos correntamente.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        binding?.tvSignInHere?.setOnClickListener {
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
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