package com.example.myapplication.model.match

data class Competition(
    val id: String,
    val name: String,
    val code: String? = "",
    val type: String,
    val emblem: String,
)
