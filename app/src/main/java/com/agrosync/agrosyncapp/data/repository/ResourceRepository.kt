package com.agrosync.agrosyncapp.data.repository

import android.util.Log
import com.agrosync.agrosyncapp.data.model.Resource
import com.google.firebase.firestore.FirebaseFirestore

class ResourceRepository {
    private var db = FirebaseFirestore.getInstance()

    fun create(resource: Resource,
               onSuccess: (String) -> Unit) {
        val documentId = db.collection("resource").document().id
        resource.id = documentId


        val data = hashMapOf(
            "id" to resource.id,
            "name" to resource.name,
            "description" to resource.description,
            "farmId" to resource.farm.id,
            "measureUnit" to resource.measureUnit
        ).apply {
            if (resource.imgUrl.isNotBlank()) this["imgUrl"] = resource.imgUrl
        }

        db.collection("resource")
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
        private const val TAG = "UserRepository"
    }
}