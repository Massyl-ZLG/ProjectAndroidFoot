package com.example.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
        /*  composable(
              route = NavigationKeys.Route.FOOD_CATEGORY_DETAILS,
              arguments = listOf(
                  navArgument(NavigationKeys.Arg.FOOD_CATEGORY_ID) {
                      type = NavType.StringType
                  }
              )
          ) {
              FoodCategoryDetailsDestination()
          }
          composable(route = NavigationKeys.Route.POSTS) {
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

object NavigationKeys {

    object Arg {
        const val TEAM_ID = "teamId"
    }

    object Route {
        const val TEAMS_LIST = "teams_list"
    }
}

