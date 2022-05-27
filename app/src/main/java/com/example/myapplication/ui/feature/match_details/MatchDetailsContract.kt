package com.example.myapplication.ui.feature.match_details


import com.example.myapplication.model.match.Match

class MatchDetailsContract {
    data class State(
        val match: Match?
    )
}