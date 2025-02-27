package com.example.myapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Age
import com.example.myapplication.domain.model.Gender
import com.example.myapplication.domain.useCase.CloseSocketUseCase
import com.example.myapplication.domain.useCase.ConnectSocketUseCase
import com.example.myapplication.domain.useCase.GetStartDialogScreenUseCase
import com.example.myapplication.domain.useCase.ReceiveSocketUseCase
import com.example.myapplication.domain.useCase.SaveStartDialogScreenUseCase
import com.example.myapplication.domain.useCase.SendScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveScreenUseCase: SaveStartDialogScreenUseCase,
    private val getScreenUseCase: GetStartDialogScreenUseCase,
    private val connectSocketUseCase: ConnectSocketUseCase,
    private val sendScreenUseCase: SendScreenUseCase,
    private val receiveSocketUseCase: ReceiveSocketUseCase,
    private val closeSocketUseCase: CloseSocketUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<StartState>(StartState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            connectSocketUseCase(
                address = SERVER_ADDRESS,
                port = SERVER_PORT
            )
        }
    }

    fun loadStartScreen() {
        viewModelScope.launch {
            _state.emit(StartState.NewScreen(getScreenUseCase()))
        }
    }

    fun userClickGender(gender: Gender) {
        getCurrentScreen()?.let { currentScreen ->
            if (currentScreen.selectedGender != gender) {
                saveScreenUseCase(currentScreen.copy(selectedGender = gender))
                loadStartScreen()
            }
        }
    }

    fun userClickAge(age: String) {
        val newAge = Age.entries.firstOrNull { it.intValue == age.toIntOrNull() } ?: Age.Unknown
        getCurrentScreen()?.let { currentScreen ->
            if (currentScreen.selectedAge != newAge) {
                saveScreenUseCase(currentScreen.copy(selectedAge = newAge))
                loadStartScreen()
            }
        }
    }

    fun userClickButton() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentScreen()?.let { screen ->
                sendScreenUseCase(screen)
                _state.emit(StartState.SendText(receiveSocketUseCase().text))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        closeSocketUseCase()
    }

    private fun getCurrentScreen() =
        (_state.value as? StartState.NewScreen)?.screen

    private companion object {
        const val SERVER_ADDRESS = "challenge.ciliz.com"
        const val SERVER_PORT = 2222
    }
}