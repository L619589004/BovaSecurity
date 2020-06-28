package com.bova.security

import com.bova.security.util.ByteUtil
import java.io.File
import java.io.OutputStream
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import java.nio.ByteBuffer
import java.util.*

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

fun main(args: Array<String>) {
    val server = ServerSocket(8888)
    val fileArray = arrayListOf(
        File("D:\\测试图片\\Jay1.jpg"),
        File("D:\\测试图片\\Jay2.jpg"),
        File("D:\\测试图片\\Jay3.jpg"),
        File("D:\\测试图片\\Jay4.jpg"),
        File("D:\\测试图片\\Jay5.jpg")
    )
    var index = 0

    println("Server is running on port ${server.localPort}")

    while (true) {
        val client = server.accept()

        val writer: OutputStream = client.getOutputStream()

        println("Client connected: ${client.inetAddress.hostAddress}")

        while (true) {
            Thread.sleep(3000L)
            try {

                val fileBytes = fileArray[0].inputStream().readBytes()
//                val bytes = ByteBuffer.allocate(4).putInt(fileBytes.size).array()
                val bytes = ByteUtil.fromUnsignedInt(fileBytes.size.toLong())

                println("Server Side:")
                println()
                println("pre 4 bytes:")
                println(bytes.toHexString())

                val combineBytes = byteArrayOf(*bytes, *fileBytes)
                println("pre 40 bytes:")
                println(combineBytes.take(40).toByteArray().toHexString())

                println("last 40 bytes:")
                println(combineBytes.takeLast(40).toByteArray().toHexString())


                writer.write(combineBytes)
                writer.flush()

                index = if (index == 4) 0 else index + 1
            } catch (e: Exception) {
                client.close()
                break
            }
        }
    }

}
