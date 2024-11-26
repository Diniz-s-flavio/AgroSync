package com.agrosync.agrosyncapp.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class AuthenticationFirebaseRepository(private val auth: FirebaseAuth) {
    fun loginWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }
    fun createUserWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun signOut() {
        auth.signOut()
    }
}