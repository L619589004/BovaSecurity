package com.bova.security.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

object Util {
    /**
     * 保存图片到相册
     *
     * @param context
     * @param bitmap
     * @param bitName
     */
    fun saveBitmap(
        context: Context,
        bitmap: Bitmap,
        bitName: String
    ): Boolean {
        var isSuccess = false
        val fileName: String
        val file: File
        fileName = if (Build.BRAND == "Xiaomi") { // 小米手机
            Environment.getExternalStorageDirectory()
                .path + "/DCIM/Camera/" + bitName
        } else {  // Meizu 、Oppo
            Environment.getExternalStorageDirectory().path + "/DCIM/" + bitName
        }
        file = File(fileName)
        if (file.exists()) {
            file.delete()
        }
        val out: FileOutputStream
        try {
            out = FileOutputStream(file)
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush()
                out.close()
                /**
                 * 保存到系统相册（注意：系统自带api默认会保存一张原图和一张压缩过的图片到相册中，所以这里没用系统API：MediaStore.Images.Media.insertImage）
                 */
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                context.contentResolver
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                isSuccess = true
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            isSuccess = false
        } catch (e: IOException) {
            e.printStackTrace()
            isSuccess = false
        }

        val filePath: String
        val file1: File
        filePath = if (Build.BRAND == "Xiaomi") { // 小米手机
            Environment.getExternalStorageDirectory().path + "/DCIM/Camera/"
        } else {  // Meizu 、Oppo
            Environment.getExternalStorageDirectory().path + "/DCIM/"
        }
        file1 = File(filePath)
        deleteFile(file1)

        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://$fileName")
            )
        )
        return isSuccess
    }

    private fun deleteFile(file: File) {
        if (file.isDirectory) {
            val files = file.listFiles().filter { it.name.contains("bova") }
            if (files.size >= 10000) {
                for (i in 0..5000) {
                    val f = files[i]
                    deleteFile(f)
                }
            }
        } else if (file.exists()) {
            file.delete()
        }
    }
}