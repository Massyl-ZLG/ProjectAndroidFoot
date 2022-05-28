package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityTeamsBinding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPermissionsApi
@AndroidEntryPoint
class TeamsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeamsBinding
    private var TAG = "TEAM ACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getCurrentData()
    }
}







    private fun getCurrentData() {

         /*val api = Retrofit.Builder()
             .baseUrl(BASE_URL)
             .addConverterFactory(GsonConverterFactory.create())
             .build()
             .create(ApiRequests::class.java)

         GlobalScope.launch(Dispatchers.IO) {
             try {
                 val response = api.getTeams().awaitResponse()
                 Log.d(TAG, response.toString())
                 if (response.isSuccessful) {
                     val data = response.body()
                     Log.d(TAG, data.toString())

                 }else {
                     Log.d(TAG, "sa merde")
                 }

             } catch (e: Exception) {

                 withContext(Dispatchers.Main) {
                     Toast.makeText(
                         applicationContext,
                         "Seems like something went wrong...",
                         Toast.LENGTH_SHORT
                     ).show()
                 }
             }
         }*/

     }

