package com.agrosync.agrosyncapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.agrosync.agrosyncapp.data.model.User
import com.agrosync.agrosyncapp.data.repository.AuthenticationFirebaseRepository
import com.agrosync.agrosyncapp.data.repository.UserRepository
import com.agrosync.agrosyncapp.ui.LoginUiState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val db: AuthenticationFirebaseRepository): ViewModel() {
    private val _authUiState = MutableStateFlow(LoginUiState.LOADING)
    val authUiState: StateFlow<LoginUiState>
        get() = _authUiState.asStateFlow()

    private var _userRepoitory = UserRepository()
    fun login(email: String, password: String) {
        viewModelScope.launch {
            db.loginWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authUiState.value = LoginUiState.SUCCESS
                } else {
                    _authUiState.value = LoginUiState.ERROR
                }
            }
        }
    }

    fun register(user: User) {
        viewModelScope.launch {
            db.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.id = task.result.user?.uid ?: ""
                    Log.d(TAG, "User ID: ${user.id}")

                    _userRepoitory.create(user)

                    _authUiState.value = LoginUiState.SUCCESS
                } else {
                    _authUiState.value = LoginUiState.ERROR
                }
            }
        }
    }


    @Suppress("UNCHECKED_CAST")
    companion object {
        private const val TAG = "AuthViewModel"
        val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            val repo = AuthenticationFirebaseRepository(Firebase.auth)
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                return AuthViewModel(repo) as T
            }
        }
    }
}