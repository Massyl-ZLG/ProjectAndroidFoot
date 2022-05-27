package com.example.myapplication.repository

import com.example.myapplication.model.State
import com.example.myapplication.model.Team
import com.example.myapplication.model.User
import com.example.myapplication.model.response.TeamsResponse
import com.example.myapplication.model.response.UserResponse

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor()  {
    private val mPostsCollection = Firebase.firestore.collection("USERS")

    fun getCurrentUser() :Flow<State<User>> = callbackFlow {
       val user = Firebase.auth.currentUser

        /* trySend(State.Loading())
        val userDocument = mPostsCollection.document("user")

        val subscription = userDocument.addSnapshotListener { snapshot, exception ->
            exception?.let {
                trySend(State.Failed(it.message.toString()))
                cancel(it.message.toString())
            }
            if (snapshot!!.exists()) {
                trySend(State.Success(snapshot.toObject(User::class.java)!!)).isSuccess
            } else {
                trySend(State.Failed("Not found")).isFailure
            }
        }
        awaitClose { subscription.remove() }*/


    }
}