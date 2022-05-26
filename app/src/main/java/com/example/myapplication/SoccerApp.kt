package com.example.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope

@HiltAndroidApp
class SoccerApp  : Application() {
    val applicationScope = GlobalScope
}