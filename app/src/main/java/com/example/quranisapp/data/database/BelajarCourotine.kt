package com.example.quranisapp.data.database

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() = runBlocking {
    val state = MutableStateFlow<Status>(Status.Loading)

    launch{
        delay(2000)
        state.emit(Status.Sucsess("Aku"))
        delay(2000)
        state.emit(Status.Loading)
        delay(2000)
        state.emit(Status.Sucsess("Anak"))
        delay(2000)
        state.emit(Status.Loading)
        delay(2000)
        state.emit(Status.Sucsess("Gufron"))
        delay(2000)
        state.emit(Status.Error("Indonesia"))
        delay(2000)
    }

    state.collectLatest { result ->
        when (result) {
            is Status.Error ->
                println("Yahaha Error")

            is Status.Loading ->
                println("Tunggu Dulu")

            is Status.Sucsess ->
                println(result.suscsesMessage)
        }
    }
}

sealed class Status {
    object Loading : Status()
    data class Sucsess(val suscsesMessage: String) : Status()
    data class Error(val errorMessage: String) : Status()
}