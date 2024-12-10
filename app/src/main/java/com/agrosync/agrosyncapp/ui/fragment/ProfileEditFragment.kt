package com.agrosync.agrosyncapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.agrosync.agrosyncapp.R
import com.agrosync.agrosyncapp.databinding.FragmentProfileEditBinding
import com.agrosync.agrosyncapp.ui.activity.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileEditFragment : Fragment() {
    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        loadUserData()

        binding.saveButton.setOnClickListener {
            saveUserData()
        }
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("user")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        binding.firstNameEditText.setText(document.getString("firstName"))
                        binding.lastNameEditText.setText(document.getString("lastName"))
                        binding.emailEditText.setText(currentUser.email)
                    }
                }
                .addOnFailureListener { e ->
                    showError("Erro ao carregar dados: ${e.message}")
                }
        }
    }

    private fun saveUserData() {
        val currentUser = auth.currentUser ?: return

        val firstName = binding.firstNameEditText.text.toString()
        val lastName = binding.lastNameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
            showError("Preencha todos os campos obrigatórios")
            return
        }

        val userUpdates = hashMapOf<String, Any>(
            "firstName" to firstName,
            "lastName" to lastName
        )

        firestore.collection("user")
            .document(currentUser.uid)
            .update(userUpdates)
            .addOnSuccessListener {

                (activity as? MainActivity)?.updateDrawerUsername()

                if (email != currentUser.email) {
                    currentUser.updateEmail(email)
                        .addOnSuccessListener {
                            if (password.isNotBlank()) {
                                updatePassword(password)
                            } else {
                                showSuccess()
                            }
                        }
                        .addOnFailureListener { e ->
                            showError("Erro ao atualizar email: ${e.message}")
                            Log.e("ProfileEditFragment", "Erro ao atualizar email: ${e.message}", e)
                        }
                } else if (password.isNotBlank()) {
                    updatePassword(password)
                } else {
                    showSuccess()
                }
            }
            .addOnFailureListener { e ->
                showError("Erro ao salvar os dados! Verifique todos os campos.")
            }
    }

    private fun updatePassword(newPassword: String) {
        val user = auth.currentUser
        user?.updatePassword(newPassword)
            ?.addOnSuccessListener {
                showSuccess()
            }
            ?.addOnFailureListener { e ->
                showError("Erro ao atualizar senha: o número mínimo de carecteres é 6!")
            }
    }

    private fun showSuccess() {
        Snackbar.make(binding.root, "Dados pessoais atualizados com sucesso!", Snackbar.LENGTH_LONG).show()
        navController.navigateUp()
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}