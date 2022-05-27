package com.example.myapplication.model.data

import android.util.Log
import com.example.myapplication.model.Player
import com.example.myapplication.model.Team
import com.example.myapplication.model.match.*
import com.example.myapplication.model.response.MatchResponse
import com.example.myapplication.model.response.MatchesResponse
import com.example.myapplication.model.response.TeamResponse
import com.example.myapplication.model.response.TeamsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SoccerRemoteSource @Inject constructor(private val SoccerApi:SoccerApi){
    private var cachedTeams: List<Team>? = null
    private var cachedPlayers: List<Player>? = null
    private var cachedMatches : List<Match>? = null

    suspend fun getTeams() : List<Team> = withContext(Dispatchers.IO){
        var cachedTeams = cachedTeams

        if (cachedTeams == null) {
            cachedTeams = SoccerApi.getTeams().mapTeamsToItems()
            this@SoccerRemoteSource.cachedTeams = cachedTeams
        }
        Log.d("SOCCER REMOTE TEAMS" , SoccerApi.getTeams().toString());
        return@withContext cachedTeams
    }

    private fun TeamsResponse.mapTeamsToItems(): List<Team> {
        return this.teams.map { team ->
            Team(
                id = team.id,
                name= team.name,
                shortName= team.shortName,
                tla=  team.tla,
                crest=  team.crest,
                address= team.address,
                website= team.website,
                founded= team.founded,
                clubColors=  team.clubColors,
                venue=  team.venue,
                lastUpdated= team.lastUpdated,
                squad = team.squad
            )
        }
    }


    suspend fun getPlayersByTeam(teamId: String) : List<Player> = withContext(Dispatchers.IO) {
        var cachedPlayers = cachedPlayers
        if (cachedPlayers == null) {
            cachedPlayers = SoccerApi.getTeam(teamId).mapPlayersToItems()
            Log.d("SOCCER REMOTE TEAM" , SoccerApi.getTeam(teamId).toString());
            this@SoccerRemoteSource.cachedPlayers = cachedPlayers
        }
        Log.d("SOCCER REMOTE PLAYERS" , cachedPlayers.toString());
        return@withContext cachedPlayers
    }

    private fun TeamResponse.mapPlayersToItems(): List<Player> {
        return this.squad.map { player->
            Player(
                id = player.id,
                name = player.name,
                position = player.position,
                dateOfBirth = player.dateOfBirth ,
                nationality = player.nationality
            )
        }
    }



    suspend fun getMatches() : List<Match> = withContext(Dispatchers.IO){
        var cachedMatches = cachedMatches

        if (cachedMatches == null) {
            cachedMatches = SoccerApi.getMatches().mapMatchesToItems()
            Log.d("SOCCER REMOTE MATCHES CACHED" , SoccerApi.getMatches().toString());
            this@SoccerRemoteSource.cachedMatches = cachedMatches
        }

        return@withContext cachedMatches
    }

    private fun MatchesResponse.mapMatchesToItems(): List<Match> {
        return this.matches.map { match ->
            Match(
                id = match.id ,
                utcDate=match.utcDate ,
                status= match.status ,
                matchday= match.matchday ,
                stage= match.stage ,
                group= match. group ,
                lastUpdated= match.lastUpdated ,
                area = match.area ,
                competition = match.competition ,
                season = match.season ,
                homeTeam =match.homeTeam  ,
                awayTeam =match.awayTeam ,
                score = match.score ,
                odds = match.odds ,
            )
        }
    }

}