package com.bova.security

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bova.security.util.ByteUtil
import java.io.ByteArrayOutputStream
import java.net.ConnectException
import java.net.Socket
import java.nio.ByteBuffer


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
    private val connection: Socket? = try {
        Socket(address, port)
    } catch (e: ConnectException) {
        callback.onSocketConnectError()
        null
    }
    private val mByteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()

    init {
        println("Connected to server at $address on port $port")

        connection?.let { socket ->
            while (socket.isConnected) {
                var imgSize = 0L
                socket.getInputStream().use {
                    var data = it.read()
                    while (data != -1) {
                        mByteArrayOutputStream.write(data)
                        if (mByteArrayOutputStream.size() == 4) {
                            //文件大小解析
//                            imgSize = ByteBuffer.wrap(mByteArrayOutputStream.toByteArray()).int
                            imgSize = ByteUtil.toUnsignedInt(mByteArrayOutputStream.toByteArray())
                            println("image size:$imgSize")
                        }
                        if (imgSize != 0L && mByteArrayOutputStream.size()
                                .toLong() == imgSize + 4
                        ) {
                            //达到文件大小时开始解析
                            println(mByteArrayOutputStream.size())
                            callback.onImageComing(
                                BitmapFactory.decodeByteArray(
                                    mByteArrayOutputStream.toByteArray(),
                                    4,
                                    imgSize.toInt()
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

}

interface ImageCallback {
    /**
     * 当Image到达时调用
     * @param image 图片对象
     */
    fun onImageComing(image: Bitmap)

    fun onSocketConnectError()
}