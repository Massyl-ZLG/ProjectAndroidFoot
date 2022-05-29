package com.example.myapplication.model

import java.util.*


data class Photo(
    var localUri: String = "",
    var remoteUri : String = "",
    var dateTaken : Date = Date(),
    var id : String = ""
    )
