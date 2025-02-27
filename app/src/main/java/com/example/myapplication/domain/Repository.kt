package com.example.myapplication.domain

import com.example.myapplication.domain.model.Message
import com.example.myapplication.domain.model.StartDialogScreen

interface Repository {

    fun loadScreen(): StartDialogScreen
    fun saveScreen(startDialogScreen: StartDialogScreen)
    fun connectSocket(
        address: String,
        port: Int
    )
    fun receiveMessageFromSocket(): Message
    fun sendScreen(request: StartDialogScreen)
    fun closeSocket()
}