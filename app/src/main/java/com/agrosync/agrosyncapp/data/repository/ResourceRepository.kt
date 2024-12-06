package com.agrosync.agrosyncapp.data.repository

import android.util.Log
import com.agrosync.agrosyncapp.data.model.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

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
            "farmId" to resource.farm!!.id,
            "measureUnit" to resource.measureUnit,
            "totalAmount" to resource.totalAmount,
            "totalValue" to resource.totalValue,
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

    suspend fun findAllResourceByFarm(farmId: String): MutableList<Resource> {
        return try {
            val querySnapshot = db.collection("resource")
                .whereEqualTo("farmId", farmId)
                .get()
                .await()

            querySnapshot.documents.mapNotNull { it.toObject(Resource::class.java) }
                .toMutableList()
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
    }

    companion object {
        private const val TAG = "UserRepository"
    }
}