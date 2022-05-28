package com.example.myapplication.ui.feature.profile

import com.example.myapplication.model.User

class ProfileContract {
    data class State(
        val user: User?,
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded : Effect()
    }
}