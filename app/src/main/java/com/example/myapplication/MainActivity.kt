package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {

    private var  TAG = "MainActivity"

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title="Welcome"
        auth = FirebaseAuth.getInstance()

    }

    fun signOut(view: View) {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    /*fun goToTeamsScreen(view: View) {
        auth.signOut()
        val intent = Intent(this, TeamsActivity::class.java)
        startActivity(intent)
        finish()
    }*/
}

