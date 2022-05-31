package com.example.myapplication.ui.feature.matches

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.data.SoccerRemoteSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel  @Inject constructor(private val remoteSource: SoccerRemoteSource) :
    ViewModel() {
    var state by mutableStateOf(
        MatchesContract.State(
            matches = listOf(),
            isLoading = true
        )
    )
        private set

    var effects = Channel<MatchesContract.Effect>(Channel.UNLIMITED)
        private set

    init {
        viewModelScope.launch { getMatches() }
    }

    private suspend fun getMatches() {
        val matches = remoteSource.getMatches()
        viewModelScope.launch {
            state = state.copy(matches = matches, isLoading = false)
            effects.send(MatchesContract.Effect.DataWasLoaded)
        }
    }
}