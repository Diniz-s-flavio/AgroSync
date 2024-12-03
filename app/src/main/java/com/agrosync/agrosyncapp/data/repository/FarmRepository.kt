package com.agrosync.agrosyncapp.data.repository

import android.util.Log
import com.agrosync.agrosyncapp.data.model.Farm
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FarmRepository {
    private var db = Firebase.firestore

    fun create(farm: Farm,
               onSuccess: (String) -> Unit) {
        val documentId = db.collection("farm").document().id
        farm.id = documentId
        Log.d(TAG,"Farm: ${farm.id}")

        val data = hashMapOf(
            "id" to farm.id,
            "ownerId" to farm.owner.id
        )


        db.collection("farm")
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
        private const val TAG = "FarmRepository"
    }
}