package com.agrosync.agrosyncapp.data.repository

import android.content.Context
import com.agrosync.agrosyncapp.util.BitmapUtils
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageRepository(
    private val context: Context,
    private val db: FirebaseFirestore
) {

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "agrosync",
            "api_key" to "963479791424758",
            "api_secret" to "AdUDfdJp2vk0Iv6shGmbW16Dudg"
        )
    )

    suspend fun uploadImage(
        imagePath: String,
        documentId: String,
        collectionName: String
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                val originalBitmap = BitmapUtils.decodeFileToBitmap(imagePath, context)

                val webpBytes = BitmapUtils.convertToWebP(originalBitmap!!)

                val uploadResult = cloudinary.uploader().upload(
                    webpBytes,
                    ObjectUtils.asMap(
                        "resource_type", "image",
                        "folder", "agrosync_image"
                    )
                )

                val imageUrl = uploadResult["secure_url"] as String

                saveImageUrlToFirestore(collectionName, documentId, imageUrl)

                imageUrl
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private suspend fun saveImageUrlToFirestore(collectionName: String, documentId: String, imageUrl: String) {
        withContext(Dispatchers.IO) {
            try {
                db.collection(collectionName)
                    .document(documentId)
                    .update("imageURL", imageUrl)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}