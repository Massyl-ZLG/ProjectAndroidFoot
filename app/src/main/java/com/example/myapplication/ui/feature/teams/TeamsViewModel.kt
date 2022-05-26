package com.example.myapplication.ui.feature.teams

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
class TeamsViewModel @Inject constructor(private val remoteSource: SoccerRemoteSource) :
    ViewModel() {
        var state by mutableStateOf(
            TeamsContract.State(
                teams = listOf(),
                isLoading = true
            )
        )
        private set

        var effects = Channel<TeamsContract.Effect>(Channel.UNLIMITED)
        private set

                init {
                    viewModelScope.launch { getFoodCategories() }
                }

        private suspend fun getFoodCategories() {
            val categories = remoteSource.getTeams()
            viewModelScope.launch {
                state = state.copy(teams = categories, isLoading = false)
                effects.send(TeamsContract.Effect.DataWasLoaded)
            }
        }
}