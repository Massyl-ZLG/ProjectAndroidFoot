package com.example.myapplication.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.example.myapplication.LoginActivity
import com.example.myapplication.ui.NavigationKeys.Arg.MATCH_ID
import com.example.myapplication.ui.NavigationKeys.Arg.PLAYER_ID
import com.example.myapplication.ui.NavigationKeys.Arg.TEAM_ID
import com.example.myapplication.ui.feature.match_details.MatchDetailsScreen
import com.example.myapplication.ui.feature.match_details.MatchDetailsViewModel
import com.example.myapplication.ui.feature.matches.MatchesScreen
import com.example.myapplication.ui.feature.matches.MatchesViewModel
import com.example.myapplication.ui.feature.profile.ProfileScreen
import com.example.myapplication.ui.feature.profile.ProfileViewModel
import com.example.myapplication.ui.feature.team_details.TeamDetailsScreen
import com.example.myapplication.ui.feature.team_details.TeamDetailsViewModel
import com.example.myapplication.ui.feature.teams.TeamsScreen
import com.example.myapplication.ui.feature.teams.TeamsViewModel
import com.example.myapplication.ui.navBar.NavBar
import com.example.myapplication.ui.theme.ComposeSampleTheme
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthAnonymousUpgradeException
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow

var user: FirebaseUser? = null

@ExperimentalPermissionsApi
@AndroidEntryPoint
class EntryPointActivity : AppCompatActivity() {
    private lateinit var  auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth= FirebaseAuth.getInstance()
        setContent {
            ComposeSampleTheme {
                TeamsApp()
            }
        }
    }

    fun goToLogin(view: View){
        val intent= Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

   /* private fun signIn(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        val  signinIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signinIntent)
    }



    private val signInLauncher = registerForActivityResult (
        FirebaseAuthUIActivityResultContract()
    ) {
            res -> this.signInResult(res)
    }



    private fun signInResult(result : FirebaseAuthUIAuthenticationResult){
        val response = result.idpResponse
        if(result.resultCode == RESULT_OK){
            user = FirebaseAuth.getInstance().currentUser
        }else  {
            Log.e( "ENTRY POINT ACTIVITY " , "ERROR IN LOGGGGGGING" + response?.error?.errorCode)
        }
    }*/
}


@ExperimentalPermissionsApi
@Preview
@Composable
private fun TeamsApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {NavBar(navController)}
    ) {
    NavHost(navController, startDestination = NavigationKeys.Route.TEAMS_LIST) {

        composable(route = NavigationKeys.Route.TEAMS_LIST) {

                TeamsDestination(navController)


        }
        composable(route = NavigationKeys.Route.MATCHES_LIST) {
            MatchesDestination(navController)
        }
        composable(
              route = NavigationKeys.Route.TEAM_DETAILS,
              arguments = listOf(
                  navArgument(NavigationKeys.Arg.TEAM_ID) {
                      type = NavType.StringType
                  }
              )
          ) {
              TeamDetailsDestination()
          }
        composable(
            route = NavigationKeys.Route.MATCH_DETAILS,
            arguments = listOf(
                navArgument(NavigationKeys.Arg.MATCH_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            MatchDetailsDestination()
        }
        composable(route = NavigationKeys.Route.PROFILE) {
            ProfileDestination()
        }

          /*composable(route = NavigationKeys.Route.POSTS) {
            PostsDestination()
        }
        composable(route = NavigationKeys.Route.LOCATION) {
            LocationDestination()
        }*/
    }
    }
}

@Composable
private fun TeamsDestination(navController: NavHostController) {
    val viewModel: TeamsViewModel = hiltViewModel()
    TeamsScreen(
        state = viewModel.state,
        effectFlow = viewModel.effects.receiveAsFlow(),
        onNavigationRequested = { itemId ->
            navController.navigate("${NavigationKeys.Route.TEAMS_LIST}/$itemId")
        }
    )
}

@Composable
private fun TeamDetailsDestination() {
    val viewModel: TeamDetailsViewModel = hiltViewModel()
    TeamDetailsScreen(viewModel.state)
}


@OptIn(ExperimentalCoilApi::class)
@Composable
private fun MatchesDestination(navController: NavHostController) {
    val viewModel: MatchesViewModel = hiltViewModel()
    MatchesScreen(
        state = viewModel.state,
        effectFlow = viewModel.effects.receiveAsFlow(),
        onNavigationRequested = { itemId ->
            navController.navigate("${NavigationKeys.Route.MATCHES_LIST}/$itemId")
        }
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun MatchDetailsDestination() {
    val viewModel: MatchDetailsViewModel = hiltViewModel()
    MatchDetailsScreen(viewModel.state)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ProfileDestination() {
    val viewModel: ProfileViewModel = hiltViewModel()
    ProfileScreen(viewModel.state)
}





@OptIn(ExperimentalCoilApi::class)
@Composable
private fun TeamDetailsPlayerDestination() {
    val viewModel: TeamDetailsViewModel = hiltViewModel()
    TeamDetailsScreen(viewModel.state)
}




object NavigationKeys {

    object Arg {
        const val TEAM_ID = "teamId"
        const val PLAYER_ID = "playerId"
        const val MATCH_ID = "matchId"
    }

    object Route {
        const val PROFILE = "profile"

        // handle matches routes
        const val MATCHES_LIST = "matches_list"
        const val MATCH_DETAILS = "$MATCHES_LIST/{$MATCH_ID}"


        // handle teams routes
        const val TEAMS_LIST = "teams_list"
        const val TEAM_DETAILS = "$TEAMS_LIST/{$TEAM_ID}"
        const val TEAM_DETAILS_PLAYER = "$TEAMS_LIST/{$TEAM_ID}/{$PLAYER_ID}"
    }
}

