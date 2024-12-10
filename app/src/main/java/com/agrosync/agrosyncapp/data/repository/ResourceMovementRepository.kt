package com.agrosync.agrosyncapp.data.repository

import android.util.Log
import com.agrosync.agrosyncapp.data.model.ResourceMovement
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ResourceMovementRepository {
    private var db = Firebase.firestore

    fun save(resourceMovement: ResourceMovement,
             onSuccess: (String) -> Unit) {
        if (resourceMovement.id.isBlank()) {
            val documentId = db.collection("resourceMovement").document().id
            resourceMovement.id = documentId
        }


        val data = hashMapOf(
            "id" to resourceMovement.id,
            "value" to resourceMovement.value,
            "userId" to resourceMovement.userId,
            "movementDate" to resourceMovement.date,
            "operation" to resourceMovement.operation,
            "resourceId" to resourceMovement.resourceId,
            "movementAmount" to resourceMovement.movementAmount,
            "oldResourceAmount" to resourceMovement.oldResourceAmount,
            "newResourceAmount" to resourceMovement.newResourceAmount,
        )

        db.collection("resourceMovement")
            .document(resourceMovement.id)
            .set(data)
            .addOnSuccessListener {
                Log.d(TAG,"Dados salvos com sucesso!")
                onSuccess(resourceMovement.id)
            }
            .addOnFailureListener { e ->
                Log.d(TAG,"Erro ao salvar os dados: ${e.message}")
            }
    }

    companion object{
        private val TAG = "ResourceMovementRepository"
    }
}