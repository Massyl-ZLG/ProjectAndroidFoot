package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String? = "",

    @SerializedName("email")
    val email: String? = "",

    @SerializedName("photoUrl")
    val photoUrl : String? = ""
)
