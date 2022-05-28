package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    var name: String? = "",

    @SerializedName("email")
    var email: String? = "",

    @SerializedName("photoUrl")
    val photoUrl : String? = ""
)
