package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.databinding.ActivityTeamsBinding
import com.example.myapplication.model.ApiRequests
import com.example.myapplication.ui.feature.teams.TeamsScreen
import com.example.myapplication.ui.feature.teams.TeamsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

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

