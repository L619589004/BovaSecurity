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
    val fileArray = arrayListOf(
        File("/Users/Srun/Desktop/Cyuanban-LoveStory1.png"),
        File("/Users/Srun/Desktop/Cyuanban-LoveStory2.png"),
        File("/Users/Srun/Desktop/Cyuanban-LoveStory3.png"),
        File("/Users/Srun/Desktop/Cyuanban-LoveStory4.png"),
        File("/Users/Srun/Desktop/Cyuanban-LoveStory5.png")
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

                val fileBytes = fileArray[index].inputStream().readBytes()
                val bytes = ByteBuffer.allocate(4).putInt(fileBytes.size).array()

                val combineBytes = byteArrayOf(*bytes, *fileBytes)

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
