package com.example.myapplication.model

import com.example.myapplication.model.response.TeamsResponse
import kotlinx.coroutines.Deferred
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path


interface ApiRequest {

    @Headers("X-Auth-Token:65e0452590374053a107528fbbfdb939")

    @GET("teams")
    fun getTeams(): Call<Team>

    fun  getTeam(@Path("id") id: Int ) : Call<Team>

}