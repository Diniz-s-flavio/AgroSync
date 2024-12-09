package com.agrosync.agrosyncapp.data.repository

import android.util.Log
import com.agrosync.agrosyncapp.data.model.Finance
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class FinanceRepository {

    private var db = Firebase.firestore
    private val financesCollection = db.collection("finance")

    fun create(finance: Finance, onSuccess: (String) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.d(TAG, "Usuário não autenticado.")
            return
        }

        val documentId = db.collection("finance").document().id
        finance.id = documentId
        Log.d(TAG, "Finance: ${finance.id}")

        val data = hashMapOf(
            "id" to finance.id,
            "userId" to userId,
            "farmId" to finance.farm?.id,
            "title" to finance.title,
            "operation" to finance.operation,
            "value" to finance.value,
            "description" to finance.description,
            "date" to finance.date
        )

        db.collection("finance")
            .document(documentId)
            .set(data)
            .addOnSuccessListener {
                Log.d(TAG, "Dados salvos com sucesso!")
                onSuccess(documentId)
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Erro ao salvar os dados: ${e.message}")
            }
    }

    fun findByUserId(userId: String, onSuccess: (List<Finance>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("finance")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val finances = mutableListOf<Finance>()
                for (document in result) {
                    val finance = document.toObject(Finance::class.java)
                    finances.add(finance)
                }
                onSuccess(finances)
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Erro ao carregar finanças: ${e.message}")
                onFailure(e)
            }
    }


    companion object {
        private const val TAG = "FinanceRepository"
    }
}