package com.example.myapplication.ui.feature.match_details

import android.util.Log
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
class MatchDetailsViewModel  @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val repository: SoccerRemoteSource
): ViewModel(){

    var state by mutableStateOf(
        MatchDetailsContract.State(
            null
        )
    )
        private set

    init {
        viewModelScope.launch {
            val matchId = stateHandle.get<String>(NavigationKeys.Arg.MATCH_ID)
                ?: throw IllegalStateException("No matchId was passed to destination.")
            val matches = repository.getMatches()
            Log.d("MATCH DETAILS VIEW" , matches.toString());
            val match = matches.first { it.id == matchId }
            state = state.copy(match = match)

        }
    }
}