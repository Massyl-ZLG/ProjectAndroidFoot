package com.example.myapplication.model.match

data class Score(

    val winner: String? = "",
    val duration:String? = "",
    val position: String? = "",
    val dateOfBirth: String,
    val nationality: String,
    val fullTime: fullTime? = null
)
