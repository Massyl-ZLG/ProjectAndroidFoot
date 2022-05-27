package com.example.myapplication.ui.feature.team_details

import com.example.myapplication.model.Player
import com.example.myapplication.model.Team

class TeamDetailsContract {
    data class State(
        val team: Team?,
        val teamPlayersItems: List<Player>,
    )
}