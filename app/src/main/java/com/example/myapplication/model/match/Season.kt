package com.example.myapplication.model.match

data class Season(
    val id: String,
    val name: String,
    val startDate: String? = "",
    val endDate: String,
    val currentMatchday: String,
    val winne: String? = ""

)
