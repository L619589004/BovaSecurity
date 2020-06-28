package com.bova.security

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.bova.security.util.ByteUtil
import java.io.ByteArrayOutputStream
import java.net.ConnectException
import java.net.Socket
import java.nio.ByteBuffer

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

fun main(args: Array<String>) {
    val address = "localhost"
    val port = 8888

    val client = Client(address, port, object : ImageCallback {
        override fun onImageComing(image: Bitmap) {
            print(image.byteCount)
        }

        override fun onSocketConnectError() {

        }
    })
}

class Client(address: String, port: Int, callback: ImageCallback) {

    private val mByteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()

    init {
        println("Connected to server at $address on port $port")

        try {
            while (true) {
                Socket(address, port).getInputStream().use { inputStream ->

                    val buffer = ByteArray(1024)
                    var read: Int
                    var imageSize = 0

                    while (inputStream.read(buffer).also { read = it } != -1) {
                        mByteArrayOutputStream.write(buffer.copyOfRange(0, read))


                        val streamSize = mByteArrayOutputStream.size()
                        if (imageSize == 0 && streamSize > 4) {
                            imageSize = ByteBuffer.wrap(
                                mByteArrayOutputStream.toByteArray().take(4).toByteArray()
                            ).int
//                            imageSize = ByteUtil.convertFourBytesToInt2(
//                                mByteArrayOutputStream.toByteArray().take(4).toByteArray()
//                            ).toInt()
                        } else if (imageSize != 0 && streamSize >= imageSize + 4) {
                            Log.e("BovaSecurity", "client receive time = " + System.currentTimeMillis())
                            callback.onImageComing(
                                BitmapFactory.decodeByteArray(
                                    mByteArrayOutputStream.toByteArray(), 4, imageSize
                                )
                            )
                            val tempByte = mByteArrayOutputStream.toByteArray()
                                .copyOfRange(imageSize + 4, streamSize)
                            mByteArrayOutputStream.reset()
                            mByteArrayOutputStream.write(tempByte)
                            imageSize = 0
                        }

                        Log.e("image Size", imageSize.toString())
                    }
                }
            }
        } catch (e: ConnectException) {
            callback.onSocketConnectError()
        }
    }

}

interface ImageCallback {
    /**
     * 当Image到达时调用
     * @param image 图片对象
     */
    fun onImageComing(image: Bitmap)

    fun onSocketConnectError()
}