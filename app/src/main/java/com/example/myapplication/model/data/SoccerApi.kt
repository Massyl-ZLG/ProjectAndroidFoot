package com.example.myapplication.model.data

import com.example.myapplication.model.response.MatchResponse
import com.example.myapplication.model.response.MatchesResponse
import com.example.myapplication.model.response.TeamResponse
import com.example.myapplication.model.response.TeamsResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import javax.inject.Inject

class SoccerApi @Inject constructor(private val service: Service){
    suspend fun getTeams(): TeamsResponse = service.getTeams()
    suspend fun getMatches(): MatchesResponse = service.getMatches()
    suspend fun getTeam(teamId : String): TeamResponse = service.getTeam(teamId)

    interface Service {
        @Headers("X-Auth-Token:65e0452590374053a107528fbbfdb939")
        @GET("teams")
        suspend fun getTeams(): TeamsResponse

        @Headers("X-Auth-Token:65e0452590374053a107528fbbfdb939")
        @GET("teams/{teamId}")
        suspend fun getTeam(@Path("teamId") teamId: String): TeamResponse


        @Headers("X-Auth-Token:65e0452590374053a107528fbbfdb939")
        @GET("matches")
        suspend fun getMatches(): MatchesResponse


    }

    companion object {
        const val BASE_URL = "https://api.football-data.org/v4/"
    }
}