package com.agrosync.agrosyncapp.data.repository

import android.util.Log
import com.agrosync.agrosyncapp.data.model.Farm
import com.agrosync.agrosyncapp.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FarmRepository {
    private var db = Firebase.firestore

    fun create(farm: Farm,
               onSuccess: (String) -> Unit) {
        val documentId = db.collection("farm").document().id
        farm.id = documentId
        Log.d(TAG,"Farm: ${farm.id}")

        val data = hashMapOf(
            "id" to farm.id,
            "ownerId" to farm.owner?.id
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

    fun findByOwnerId(uid: String, onSuccess: (Farm?) -> Unit) {
        db.collection("farm")
            .whereEqualTo("ownerId", uid).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val farm = documents.documents[0].toObject(Farm::class.java)
                    onSuccess(farm)
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("HomeFragment", "Erro ao buscar o usuário", e)
                onSuccess(null)
            }
    }
    suspend fun findByOwnerId(uid: String): Farm? {
        val result = db.collection("farm")
            .whereEqualTo("ownerId", uid)
            .get()
            .await()

        if (!result.isEmpty) {
            return result.documents[0].toObject(Farm::class.java)
        } else {
            Log.e("HomeFragment", "Erro ao buscar o Fazenda")
            return null
        }
    }

    companion object {
        private const val TAG = "FarmRepository"
    }
}