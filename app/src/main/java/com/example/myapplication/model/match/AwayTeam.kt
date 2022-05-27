package com.example.myapplication.model.match

data class AwayTeam(
    val id: String,
    val name: String,
    val shortName: String? = "",
    val tla: String,
    val crest: String,
)
