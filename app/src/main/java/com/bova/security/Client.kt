package com.bova.security

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bova.security.util.ByteUtil
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.*
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
    private var socket: DatagramSocket? = null

    init {
        println("Connected to server at $address on port $port")

        try {
            socket = DatagramSocket(InetSocketAddress( 8888))

            println("***************开始监听消息***************")
            while (true) {
                val data = ByteArray(1024 * 64)
                val packet = DatagramPacket(data, data.size)
                socket?.receive(packet)

                callback.onImageComing(
                    BitmapFactory.decodeByteArray(
                        data, packet.offset, packet.length
                    )
                )
            }
        } catch (e: SocketException) {
            e.printStackTrace()
            callback.onSocketConnectError()
        } catch (e: IOException) {
            e.printStackTrace()
            callback.onSocketConnectError()
        } finally {
            socket?.close()
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