package com.example.myapplication.ui.feature.team_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.data.SoccerRemoteSource
import com.example.myapplication.ui.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamDetailsViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val repository: SoccerRemoteSource
): ViewModel(){

    var state by mutableStateOf(
        TeamDetailsContract.State(
            null, listOf(
            )
        )
    )
        private set

    init {
        viewModelScope.launch {
            val teamId = stateHandle.get<String>(NavigationKeys.Arg.TEAM_ID)
                ?: throw IllegalStateException("No teamId was passed to destination.")
            val teams = repository.getTeams()
            val team = teams.first { it.id == teamId }
            state = state.copy(team = team)
            val playerItems = repository.getPlayersByTeam(teamId)
            state = state.copy(teamPlayersItems = playerItems)
        }
    }
}