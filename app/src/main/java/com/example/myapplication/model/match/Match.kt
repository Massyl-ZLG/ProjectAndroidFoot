package com.example.myapplication.model.match

import com.example.myapplication.model.Player
import com.google.gson.annotations.SerializedName

data class Match(
    val id: String,
    val utcDate: String,
    val status: String? = "",
    val matchday: String,
    val stage: String,
    val group: String,
    val lastUpdated: String,
    val area : Area? = null,
    val competition : Competition? = null,
    val season : Season? = null,
    val homeTeam : HomeTeam? = null,
    val awayTeam : AwayTeam? = null,
    val score : Score? = null,
    val odds : Odds? = null,

)
