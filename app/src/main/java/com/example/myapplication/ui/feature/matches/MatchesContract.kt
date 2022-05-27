package com.example.myapplication.ui.feature.matches

import com.example.myapplication.model.match.Match

class MatchesContract {
    data class State(
        val matches: List<Match> = listOf(),
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded : Effect()
    }
}