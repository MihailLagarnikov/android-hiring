package com.example.myapplication.data.network

import android.util.Log
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket


private const val TAG = "SocketManager"

class SocketManager(
    private val address: String,
    private val port: Int
) {
    private var socket: Socket? = null

    fun connect() {
        socket = Socket(address, port)
        Log.d(TAG, "connected: ${socket?.isConnected}")
    }

    fun send(request: TestRequest) {
        socket?.let {
            val message = "{ \"gender\": \"${request.gender}\", \"age\": ${request.age} }"

            Log.i(TAG, "sending: $message")
            val out = BufferedWriter(OutputStreamWriter(it.getOutputStream()))
            out.write(message + "\n")
            out.flush()

        }
    }

    fun receive(): TestResponse {
        return socket?.let {
            val inn = BufferedReader(InputStreamReader(it.getInputStream()))
            val message = inn.readLine()
            Log.d(TAG, "received: $message")
            TestResponse(message.contains("true"))
        } ?: kotlin.run {
            TestResponse(false)
        }
    }

    fun close() {
        socket?.close()
        socket = null
    }
}
