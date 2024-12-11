package com.agrosync.agrosyncapp.util

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtils {

    fun getPath(context: Context, uri: Uri): String? {
        return try {
            val file = createFileFromUri(context, uri)
            file?.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun createFileFromUri(context: Context, uri: Uri): File? {
        val contentResolver: ContentResolver = context.contentResolver
        val fileName = getFileName(contentResolver, uri)
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, fileName ?: "temp_file")
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return file
    }

    private fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
        var name: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index >= 0) {
                        name = it.getString(index)
                    }
                }
            }
        } else {
            name = uri.lastPathSegment
        }
        return name
    }
}