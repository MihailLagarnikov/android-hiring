package com.example.myapplication.presentation

import com.example.myapplication.domain.model.StartDialogScreen

sealed class StartState(val isLoading: Boolean) {
    data object Loading : StartState(true)
    class NewScreen(val screen: StartDialogScreen) : StartState(false)
    class SendText(val text: String) : StartState(false)
}