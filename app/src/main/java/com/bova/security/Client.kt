package com.bova.security

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.net.Socket
import java.nio.ByteBuffer


fun main(args: Array<String>) {
    val address = "localhost"
    val port = 8888

    val client = Client(address, port, object : ImageCallback {
        override fun onImageComing(image: Bitmap) {
            print(image.byteCount)
        }
    })
}

class Client(address: String, port: Int, callback: ImageCallback) {
    private val connection: Socket = Socket(address, port)
    private val mByteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()

    init {
        println("Connected to server at $address on port $port")

        while (connection.isConnected) {
            var imgSize = 0
            connection.getInputStream().use {
                var data = it.read()
                while (data != -1) {
                    mByteArrayOutputStream.write(data)
                    if (mByteArrayOutputStream.size() == 4) {
                        //文件大小解析
                        imgSize = ByteBuffer.wrap(mByteArrayOutputStream.toByteArray()).int
                        println("image size:$imgSize")
                    }
                    if (imgSize != 0 && mByteArrayOutputStream.size() == imgSize + 4) {
                        //达到文件大小时开始解析
                        println(mByteArrayOutputStream.size())
                        callback.onImageComing(
                            BitmapFactory.decodeByteArray(
                                mByteArrayOutputStream.toByteArray(),
                                4,
                                imgSize
                            )
                        )
                        mByteArrayOutputStream.reset()
                    }
                    data = it.read()
                }
                print(mByteArrayOutputStream.size())
            }
        }
    }

}

interface ImageCallback {
    /**
     * 当Image到达时调用
     * @param image 图片对象
     */
    fun onImageComing(image: Bitmap)
}