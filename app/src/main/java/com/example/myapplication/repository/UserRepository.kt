package com.example.myapplication.repository

import android.util.Log
import com.example.myapplication.model.User

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor()  {


    fun getCurrentUser() : User  {
        val user = Firebase.auth.currentUser
        val firestore = Firebase.firestore
        //val photoUrl = user?.let { firestore.collection("users").document(it.uid).collection("profile_pictures").get() }
        //Log.i("USER REPO" , photoUrl.toString() )
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