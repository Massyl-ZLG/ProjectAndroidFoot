package com.example.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.myapplication.ui.NavigationKeys.Arg.PLAYER_ID
import com.example.myapplication.ui.NavigationKeys.Arg.TEAM_ID
import com.example.myapplication.ui.feature.team_details.TeamDetailsScreen
import com.example.myapplication.ui.feature.team_details.TeamDetailsViewModel
import com.example.myapplication.ui.feature.teams.TeamsScreen
import com.example.myapplication.ui.feature.teams.TeamsViewModel
import com.example.myapplication.ui.theme.ComposeSampleTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow

@ExperimentalPermissionsApi
@AndroidEntryPoint
class EntryPointActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSampleTheme {
                TeamsApp()
            }
        }
    }
}


@ExperimentalPermissionsApi
@Preview
@Composable
private fun TeamsApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationKeys.Route.TEAMS_LIST) {
        composable(route = NavigationKeys.Route.TEAMS_LIST) {
            TeamsDestination(navController)
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
          /*composable(route = NavigationKeys.Route.POSTS) {
            PostsDestination()
        }
        composable(route = NavigationKeys.Route.LOCATION) {
            LocationDestination()
        }*/
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

/*
@Composable
private fun TeamDetailsPlayerDestination() {
    val viewModel: TeamDetailsViewModel = hiltViewModel()
    TeamDetailsScreen(viewModel.state)
}
*/


object NavigationKeys {

    object Arg {
        const val TEAM_ID = "teamId"
        const val PLAYER_ID = "playerId"
    }

    object Route {

        const val TEAMS_LIST = "teams_list"
        const val TEAM_DETAILS = "$TEAMS_LIST/{$TEAM_ID}"
        const val TEAM_DETAILS_PLAYER = "$TEAMS_LIST/{$TEAM_ID}/{$PLAYER_ID}"
    }
}

