package com.example.myapplication.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences.Editor
import com.example.myapplication.data.network.SocketManager
import com.example.myapplication.data.network.TestRequest
import com.example.myapplication.domain.Repository
import com.example.myapplication.domain.model.Age
import com.example.myapplication.domain.model.Gender
import com.example.myapplication.domain.model.Message
import com.example.myapplication.domain.model.StartDialogScreen
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val context: Context
) : Repository {
    private val preferences by lazy {
        context.getSharedPreferences(SHAPER_PREF, MODE_PRIVATE)
    }
    private var socketManager: SocketManager? = null

    override fun loadScreen(): StartDialogScreen {
        return StartDialogScreen(
            selectedGender = Gender.valueOf(preferences.getString(GENDER_KEY, DEFAULT_VALUE) ?: ""),
            selectedAge = Age.entries.firstOrNull {
                it.intValue == preferences.getInt(
                    AGE_KEY,
                    DEFAULT_VALUE_INT
                )
            } ?: Age.Unknown
        )
    }

    override fun saveScreen(startDialogScreen: StartDialogScreen) {
        val ed: Editor = preferences.edit()
        ed.putString(GENDER_KEY, startDialogScreen.selectedGender.name)
        ed.putInt(AGE_KEY, startDialogScreen.selectedAge.intValue)
        ed.apply()
    }

    override fun connectSocket(
        address: String,
        port: Int
    ) {
        socketManager = SocketManager(
            address = address,
            port = port
        )
        socketManager?.connect()
    }

    override fun receiveMessageFromSocket(): Message {
        return Message(
            text = if (socketManager?.receive()?.allowed == true) {
                "Success"
            } else {
                "Failure"
            }
        )
    }

    override fun sendScreen(request: StartDialogScreen) {
        socketManager?.send(
            TestRequest(
                gender = request.selectedGender.value,
                age = request.selectedAge.intValue
            )
        )
    }

    override fun closeSocket() {
        socketManager?.close()
    }
}

private const val SHAPER_PREF = "SHARED_PREF"
private const val GENDER_KEY = "GENDER_KEY"
private const val AGE_KEY = "AGE_KEY"
private const val DEFAULT_VALUE = "Unknown"
private const val DEFAULT_VALUE_INT = 0