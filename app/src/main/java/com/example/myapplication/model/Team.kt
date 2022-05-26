package com.example.myapplication.model

data class Team(
    val id: String,
    val name: String,
    val shortName: String,
    val tla: String,
    val crest: String  = "",
    val address: String,
    val website: String,
    val founded: Int,
    val clubColors: String = "",
    val venue: String = "",
    val lastUpdated: String,
)
