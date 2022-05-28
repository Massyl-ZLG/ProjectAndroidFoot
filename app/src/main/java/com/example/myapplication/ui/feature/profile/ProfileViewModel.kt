package com.example.myapplication.ui.feature.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.State
import com.example.myapplication.model.User
import com.example.myapplication.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel  @Inject constructor(
         private val repository: UserRepository
) : ViewModel() {

    init {
        getUser()
    }


    val state by mutableStateOf(
        ProfileContract.State(
        user =null,
        isLoading = true
        )
    )

    private fun getUser() {
        repository.getCurrentUser()
    }

   /* fun addPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPost(post).collect {
                mState.value = it
            }
        }
    }*/
}