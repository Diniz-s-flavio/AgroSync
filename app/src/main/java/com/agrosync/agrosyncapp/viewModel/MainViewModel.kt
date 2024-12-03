package com.agrosync.agrosyncapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.agrosync.agrosyncapp.data.repository.AuthenticationFirebaseRepository
import com.agrosync.agrosyncapp.ui.LoginUiState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(private val db: AuthenticationFirebaseRepository): ViewModel() {
    private val _authUiState = MutableStateFlow(LoginUiState.LOADING)
    val authUiState: StateFlow<LoginUiState>
        get() = _authUiState.asStateFlow()

    @Suppress("UNCHECKED_CAST")
    companion object {
        private const val TAG = "MainViewModel"
        val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            val repo = AuthenticationFirebaseRepository(Firebase.auth)
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                return AuthViewModel(repo) as T
            }
        }
    }
}