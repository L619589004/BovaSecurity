package com.bova.security

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.bova.security.util.ByteUtil
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.IllegalArgumentException
import java.net.*
import java.nio.ByteBuffer
import kotlin.concurrent.thread

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

fun main(args: Array<String>) {
    val address = "localhost"
    val port = 8888

    val client = Client(address, port, object : ImageCallback {
        override fun onImageComing(image: Bitmap, isNeedAlarm: Boolean) {
            print("isNeedAlarm = " + isNeedAlarm)
        }

        override fun onSocketConnectError() {

        }
    })
}

class Client(address: String, port: Int, callback: ImageCallback) {

    private val mByteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
    private val speedList = mutableListOf<Double>()
    private var socket: DatagramSocket? = null
    private var flag = false

    init {
        thread {
            println("Connected to server at $address on port $port")

            try {
                /**
                 * send a message to device
                 */
                socket = DatagramSocket()
                val serverAddress = InetSocketAddress(address, 8888)
                val sendData = "hello".toByteArray()
                val sendPacket = DatagramPacket(sendData, sendData.size, serverAddress)
                socket?.send(sendPacket)

                socket = DatagramSocket(InetSocketAddress(8888))

                flag = true

                println("***************开始监听消息***************")
                while (flag) {
                    val data = ByteArray(1024 * 64)
                    val packet = DatagramPacket(data, data.size)
                    socket?.receive(packet)

                    callback.onImageComing(
                        image = BitmapFactory.decodeByteArray(
                            data,
                            packet.offset + 1,
                            packet.length
                        ), isNeedAlarm = data[0] == 0X01.toByte()
                    )
                }
            } catch (e: SocketException) {
                e.printStackTrace()
                if (flag) {
                    callback.onSocketConnectError()
                }
                Log.e("Client", "SocketException---$e")
            } catch (e: IOException) {
                e.printStackTrace()
                callback.onSocketConnectError()
                Log.e("Client", "IOException---$e")
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } finally {
                socket?.close()
            }
        }
    }

    fun reset() {
        flag = false
        socket?.close()
        socket = null
    }
}

interface ImageCallback {
    /**
     * 当Image到达时调用
     *
     * @param image 图片对象
     * @param isNeedAlarm 是否需要报警
     */
    fun onImageComing(image: Bitmap, isNeedAlarm: Boolean)

    fun onSocketConnectError()
}