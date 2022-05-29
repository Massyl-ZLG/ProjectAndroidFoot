package com.example.myapplication.ui.feature.profile


import android.net.Uri
import com.example.myapplication.model.User
import com.example.myapplication.ui.EntryPointActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi

class ProfileContract {
    data class State @OptIn(ExperimentalPermissionsApi::class) constructor(
        val user: User?,
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded : Effect()
    }
}