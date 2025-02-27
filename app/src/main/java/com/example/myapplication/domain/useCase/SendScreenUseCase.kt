package com.example.myapplication.domain.useCase

import com.example.myapplication.domain.Repository
import com.example.myapplication.domain.model.StartDialogScreen
import javax.inject.Inject

class SendScreenUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(startDialogScreen: StartDialogScreen) =
        repository.sendScreen(startDialogScreen)
}