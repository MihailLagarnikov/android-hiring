package com.example.myapplication.domain.useCase

import com.example.myapplication.domain.Repository
import javax.inject.Inject

class CloseSocketUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke() = repository.closeSocket()
}