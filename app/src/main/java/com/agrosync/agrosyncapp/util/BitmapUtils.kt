package com.agrosync.agrosyncapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream

object BitmapUtils {

    fun convertToWebP(bitmap: Bitmap, quality: Int = 100): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.WEBP, quality, outputStream)
        return outputStream.toByteArray()
    }

    fun decodeFileToBitmap(filePath: String, context: Context): Bitmap? {
        return try {
            // Usa o ContentResolver para acessar o conteúdo do URI
            context.contentResolver.openInputStream(Uri.parse(filePath)).use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}