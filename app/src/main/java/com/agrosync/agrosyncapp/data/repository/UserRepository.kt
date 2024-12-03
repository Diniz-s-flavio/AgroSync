package com.agrosync.agrosyncapp.data.repository

import android.util.Log
import com.agrosync.agrosyncapp.data.model.Farm
import com.agrosync.agrosyncapp.data.model.User
import com.agrosync.agrosyncapp.data.repository.FarmRepository.Companion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()
    private var farmRepository = FarmRepository()

    fun create(user: User) {
        val farm = Farm("", user)
        farmRepository.create(farm,
            onSuccess = { farmId ->

                val data = hashMapOf(
                    "id" to user.id,
                    "firstName" to user.firstName,
                    "lastName" to user.lastName,
                    "email" to user.email,
                    "role" to user.role,
                    "ownedFarms" to farmId
                )

                db.collection("user")
                    .document(user.id)
                    .set(data).addOnSuccessListener {
                        Log.d(TAG,"Dados salvos com sucesso!")
                    }
                    .addOnFailureListener { e ->
                        Log.d(TAG,"Erro ao salvar os dados: ${e.message}")
                    }})
    }

    suspend fun getUserName(): String {
        val userId = auth.currentUser?.uid ?: throw IllegalArgumentException("Usuário não autenticado")
        return try {
            val userDocument = db.collection("user")
                .document(userId)
                .get()
                .await()

            userDocument.getString("firstName") ?: "Usuário"
        } catch (e: Exception) {
            "Usuário"
        }
    }

    companion object {
        private const val TAG = "UserRepository"
    }
}