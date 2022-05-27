package com.example.myapplication.model.response

import com.example.myapplication.model.Player
import com.example.myapplication.model.match.*
import com.google.gson.annotations.SerializedName


data class TeamsResponse(val teams: List<TeamResponse>)

data class TeamResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("shortName") val shortName: String,
    @SerializedName("tla") val tla: String,
    @SerializedName("crest") val crest:  String,
    @SerializedName("address") val address: String,
    @SerializedName("website") val website: String,
    @SerializedName("founded") val founded: Int,
    @SerializedName("clubColors") val clubColors: String ,
    @SerializedName("venue") val venue: String ,
    @SerializedName("lastUpdated") val lastUpdated: String,
    @SerializedName("squad") val squad: List<Player>,
)


data class  PlayersResponse(val players: List<PlayerResponse>)

data class  PlayerResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("position") val position: String,
    @SerializedName("dateOfBirth") val dateOfBirth: String,
    @SerializedName("nationality") val nationality: String,
)

data class MatchesResponse(val matches: List<MatchResponse>)

data class  MatchResponse(
    @SerializedName("id") val id: String,
    @SerializedName("utcDate") val utcDate: String,
    @SerializedName("status") val status: String,
    @SerializedName("matchday") val matchday: String,
    @SerializedName("stage") val stage: String,
    @SerializedName("group") val group: String,
    @SerializedName("lastUpdated") val lastUpdated: String,
    @SerializedName("area") val area: Area,
    @SerializedName("competition") val competition: Competition,
    @SerializedName("season") val season: Season,
    @SerializedName("homeTeam") val homeTeam: HomeTeam,
    @SerializedName("awayTeam") val awayTeam: AwayTeam,
    @SerializedName("score") val score: Score,
    @SerializedName(" odds") val  odds: Odds
)



