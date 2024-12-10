package com.agrosync.agrosyncapp.data.repository

import android.util.Log
import com.agrosync.agrosyncapp.data.model.Farm
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FarmRepository {
    private val db = FirebaseFirestore.getInstance()

    fun create(farm: Farm, onSuccess: (String) -> Unit) {
        // Método `create` mantido sem alterações
        val documentId = db.collection("farm").document().id
        farm.id = documentId
        Log.d(TAG, "Farm: ${farm.id}")

        val data = hashMapOf(
            "id" to farm.id,
            "ownerId" to farm.owner?.id
        )

        db.collection("farm")
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

    fun findByOwnerId(uid: String, onSuccess: (Farm?) -> Unit) {
        db.collection("farm")
            .whereEqualTo("ownerId", uid)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val farm = documents.documents[0].toObject(Farm::class.java)?.apply {
                        id = documents.documents[0].id
                    }
                    onSuccess(farm)
                } else {
                    Log.d(TAG, "Nenhuma fazenda encontrada para o proprietário com ID: $uid")
                    onSuccess(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Erro ao buscar a fazenda por ownerId: ${e.message}", e)
                onSuccess(null)
            }
    }

    suspend fun findByOwnerId(userUid: String): Farm? {
        val result = db.collection("farm").whereEqualTo("ownerId", userUid).get().await()
        return result.documents.firstOrNull()?.toObject(Farm::class.java)
    }

    companion object {
        private const val TAG = "FarmRepository"
    }
}
