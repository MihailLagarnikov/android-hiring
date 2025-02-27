package com.example.myapplication.domain.useCase

import com.example.myapplication.domain.Repository
import javax.inject.Inject

class ConnectSocketUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(
        address: String,
        port: Int
    ) = repository.connectSocket(
        address = address,
        port = port
    )
}