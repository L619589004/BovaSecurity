package com.bova.security.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileObserver
import android.util.Log
import java.io.File


internal class BovaFileObserver(private val mPath: String, val mContext: Context) :
    FileObserver(mPath, CREATE) {

    private var isDeleting = false

    override fun onEvent(event: Int, path: String?) {
        if (event == CREATE) {
            if (!isDeleting)
                deleteFile(File(mPath))
        }
    }

    private fun deleteFile(file: File) {
        if (file.isDirectory) {
            val files = file.listFiles().filter { it.isFile && it.name.endsWith("_bova.JPEG") }

            if (files.size >= 10000) {
                isDeleting = true
                files.take(5000).forEachIndexed { index, f ->
                    if (f.exists()) {
                        f.delete()
                    }
                    if (index == 4999) {
                        isDeleting = false
                        notifyGalleryUpdate(file)
                        Log.e("BovaFileObserver", "delete success")
                    }
                }
            }
        }
    }

    private fun notifyGalleryUpdate(outputFile: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentUri = Uri.fromFile(outputFile)
            scanIntent.data = contentUri
            mContext.sendBroadcast(scanIntent)
        } else {
            val intent = Intent(
                Intent.ACTION_MEDIA_MOUNTED,
                Uri.parse("file://" + Environment.getExternalStorageDirectory())
            )
            mContext.sendBroadcast(intent)
        }
    }
}