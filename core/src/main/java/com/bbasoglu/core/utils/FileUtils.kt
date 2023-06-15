package com.bbasoglu.core.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

object FileUtils {
    fun isFileLessThan(file: File, maxFileSize: Long): Boolean {
        val l = file.length()
        val fileSize = l.toString()
        val finalFileSize = fileSize.toInt()
        return finalFileSize >= maxFileSize
    }
    fun getFileFromUri(uriFile: Uri, context: Context): File? {
        uriFile.path?.let { path->
            var realPath = String()
            val databaseUri: Uri
            val selection: String?
            val selectionArgs: Array<String>?
            if (path.contains("/document/image:")) {
                databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                selection = "_id=?"
                selectionArgs = arrayOf(DocumentsContract.getDocumentId(uriFile).split(":")[1])
            } else {
                databaseUri = uriFile
                selection = null
                selectionArgs = null
            }
            try {
                val column = "_data"
                val projection = arrayOf(column)
                val cursor = context.contentResolver.query(
                    databaseUri,
                    projection,
                    selection,
                    selectionArgs,
                    null
                )
                cursor?.let {
                    if (it.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndexOrThrow(column)
                        realPath = cursor.getString(columnIndex)
                    }
                    cursor.close()
                }
            } catch (e: Exception) {
                Log.e("GetFileUri Exception:", "exception file")
            }
            val pathResult = if (realPath.isNotEmpty()) realPath else {
                when {
                    path.contains("/document/raw:") -> path.replace(
                        "/document/raw:",
                        ""
                    )
                    path.contains("/document/primary:") -> path.replace(
                        "/document/primary:",
                        "/storage/emulated/0/"
                    )
                    else -> return null
                }
            }
            return File(pathResult)
        }?:let {
            return null
        }
    }
    fun getFile(mContext: Activity?, documentUri: Uri): File {
        val inputStream = mContext?.contentResolver?.openInputStream(documentUri)
        var file =  File("")
        inputStream.use { input ->
            file =
                File(mContext?.cacheDir, System.currentTimeMillis().toString()+".pdf")
            FileOutputStream(file).use { output ->
                val buffer =
                    ByteArray(4 * 1024) // or other buffer size
                var read: Int = -1
                while (input?.read(buffer).also {
                        if (it != null) {
                            read = it
                        }
                    } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        }
        return file
    }
    fun getReadableFileSize(size: Long): String {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }
}