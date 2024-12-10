package com.agrosync.agrosyncapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.agrosync.agrosyncapp.databinding.FragmentPasswordRecoveryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PasswordRecoveryFragment : Fragment() {

    private var _binding: FragmentPasswordRecoveryBinding? = null
    private val binding get() = _binding!!
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPasswordRecoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSendRecovery.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()

            if (email.isNotEmpty()) {
                checkIfEmailExists(email)
            } else {
                Toast.makeText(requireContext(), "Por favor, insira um e-mail válido.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkIfEmailExists(email: String) {
        firestore.collection("user")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    sendPasswordRecoveryEmail(email)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "E-mail não cadastrado no sistema.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    requireContext(),
                    "Erro ao verificar o e-mail: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun sendPasswordRecoveryEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "Verifique seu e-mail para redefinir a senha.",
                    Toast.LENGTH_LONG
                ).show()

                requireActivity().onBackPressed()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    requireContext(),
                    "Erro ao enviar o link: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

