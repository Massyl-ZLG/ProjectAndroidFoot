package com.example.myapplication.repository

import com.example.myapplication.model.User

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor()  {


    fun getCurrentUser() : User?  {
       val user = Firebase.auth.currentUser

        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
        }

        if (user != null) {
            return User(
                id = user.uid,
                name =  user.displayName,
                email = user.email,
                photoUrl =  user.photoUrl.toString()
                )
        }

        return User(
            id = "1",
            name =  "user.displayName",
            email = "user.email",
            photoUrl =  "user.photoUrl.toString()"
        )
    }
}