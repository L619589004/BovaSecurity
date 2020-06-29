package com.bova.security

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
    private val speedList = mutableListOf<Double>()

    init {
        println("Connected to server at $address on port $port")

        try {
            while (true) {
                Socket(address, port).getInputStream().use { inputStream ->

                    val buffer = ByteArray(1024)
                    var read: Int
                    var imageSize = 0

                    var startTime = 0L

                    while (inputStream.read(buffer).also { read = it } != -1) {
                        if (startTime == 0L) {
                            startTime = System.currentTimeMillis()
                        }

                        mByteArrayOutputStream.write(buffer.copyOfRange(0, read))


                        val streamSize = mByteArrayOutputStream.size()
                        if (imageSize == 0 && streamSize > 4) {
//                            imageSize = ByteBuffer.wrap(
//                                mByteArrayOutputStream.toByteArray().take(4).toByteArray()
//                            ).int
                            imageSize = ByteUtil.convertFourBytesToInt2(
                                mByteArrayOutputStream.toByteArray().take(4).toByteArray()
                            ).toInt()
                        } else if (imageSize != 0 && streamSize >= imageSize + 4) {
                            callback.onImageComing(
                                BitmapFactory.decodeByteArray(
                                    mByteArrayOutputStream.toByteArray(), 4, imageSize
                                )
                            )
                            val tempByte = mByteArrayOutputStream.toByteArray()
                                .copyOfRange(imageSize + 4, streamSize)
                            mByteArrayOutputStream.reset()
                            mByteArrayOutputStream.write(tempByte)
                            val useTime = System.currentTimeMillis() - startTime
                            val imageSpeed = imageSize / 1024.0 / useTime * 1000
                            speedList.add(imageSpeed)
                            if (speedList.size > 10) {

                                println(
                                    "average speed: ${speedList.takeLast(10).average()}kb"
                                )
                            }
//                            println("image size: ${imageSize / 1024}kb" + " time: " + useTime + "ms")
                            imageSize = 0
                            startTime = 0L
                        }
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