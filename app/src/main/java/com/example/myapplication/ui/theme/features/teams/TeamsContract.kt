package com.example.myapplication.ui.theme.features.teams

import com.example.myapplication.model.Team

class TeamsContract {

    data class State(
        val teams: List<Team> = listOf(),
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded : Effect()
    }
}