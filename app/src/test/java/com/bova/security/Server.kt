package com.bova.security

import java.io.File
import java.io.OutputStream
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import java.nio.ByteBuffer
import java.util.*


fun main(args: Array<String>) {
    val server = ServerSocket(8888)
    val file = File("/Users/Srun/Desktop/Cyuanban-LoveStory5.png")


    println("Server is running on port ${server.localPort}")

    while (true) {
        val client = server.accept()

        val writer: OutputStream = client.getOutputStream()

        println("Client connected: ${client.inetAddress.hostAddress}")

        while (true) {
            Thread.sleep(3000L)
            try {

                val fileBytes = file.inputStream().readBytes()
                val bytes = ByteBuffer.allocate(4).putInt(fileBytes.size).array()

                val combineBytes = byteArrayOf(*bytes, *fileBytes)

                writer.write(combineBytes)
                writer.flush()

            } catch (e: Exception) {
                client.close()
                break
            }
        }
    }

}
