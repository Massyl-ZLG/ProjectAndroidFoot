package com.example.myapplication.model.data

import android.util.Log
import com.example.myapplication.model.Team
import com.example.myapplication.model.response.TeamsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SoccerRemoteSource @Inject constructor(private val SoccerApi:SoccerApi){
    private var cachedTeams: List<Team>? = null

    suspend fun getTeams() : List<Team> = withContext(Dispatchers.IO){
        var cachedTeams = cachedTeams
        if (cachedTeams == null) {
            cachedTeams = SoccerApi.getTeams().mapTeamsToItems()
            this@SoccerRemoteSource.cachedTeams = cachedTeams
        }
        Log.d("SOCCER REMOTE" , cachedTeams.toString());
        return@withContext cachedTeams
    }

    private fun TeamsResponse.mapTeamsToItems(): List<Team> {
        return this.teams.map { team ->
            Team(
                id = team.id,
                name= team.name,
                shortName= team.shortName,
                tla=  "" , // team.tla,
                crest= "" , // team.crest,
                address= team.address,
                website= team.website,
                founded= team.founded,
                clubColors=  ""  , // team.clubColors
                venue= "" , // team.venue
                lastUpdated= team.lastUpdated,
            )
        }
    }
}