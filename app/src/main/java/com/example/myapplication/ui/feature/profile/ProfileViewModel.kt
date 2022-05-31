package com.example.myapplication.ui.feature.profile


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModel

import com.example.myapplication.repository.UserRepository

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject
@HiltViewModel
class ProfileViewModel  @OptIn(ExperimentalPermissionsApi::class)
@Inject constructor(
    private val repository: UserRepository
) : ViewModel() {


    var state by mutableStateOf(
    ProfileContract.State(
    user = repository.getCurrentUser(),
    isLoading = true
    )
)
}

