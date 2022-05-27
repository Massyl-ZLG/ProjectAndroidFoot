package com.example.myapplication.ui.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.State
import com.example.myapplication.model.User
import com.example.myapplication.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel  @Inject constructor(
         private val repository: UserRepository
) : ViewModel() {

    init {
        getUser()
    }

    private val mState = MutableStateFlow<State<User>>(State.loading())
    val state: StateFlow<State<User>>
        get() = mState

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getCurrentUser().collect {
                    mState.value = it
                }
            } catch (ex: Exception) {
                mState.value = State.failed(ex.localizedMessage)
            }
        }
    }

   /* fun addPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPost(post).collect {
                mState.value = it
            }
        }
    }*/
}