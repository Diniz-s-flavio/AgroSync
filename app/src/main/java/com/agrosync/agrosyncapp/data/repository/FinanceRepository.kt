package com.agrosync.agrosyncapp.data.repository

import android.util.Log
import com.agrosync.agrosyncapp.data.model.Finance
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FinanceRepository {

    private var db = Firebase.firestore

    fun create(finance: Finance,
               onSuccess: (String) -> Unit) {
        val documentId = db.collection("finance").document().id
        finance.id = documentId
        Log.d(TAG,"Finance: ${finance.id}")

        val data = hashMapOf(
            "id" to finance.id,
            "farmId" to finance.farm?.id,
            "title" to finance.title,
            "operation" to finance.operation,
            "value" to finance.value,
            "description" to finance.description,
            "date" to finance.date,
        )


        db.collection("finance")
            .document(documentId)
            .set(data)
            .addOnSuccessListener {
                Log.d(TAG,"Dados salvos com sucesso!")
                onSuccess(documentId)
            }
            .addOnFailureListener { e ->
                Log.d(TAG,"Erro ao salvar os dados: ${e.message}")
            }
    }

    companion object {
        private const val TAG = "FinanceRepository"
    }
}