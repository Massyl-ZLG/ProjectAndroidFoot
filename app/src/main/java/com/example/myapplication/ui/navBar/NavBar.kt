package com.example.myapplication.ui.navBar

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.compose.material.Icon
import androidx.compose.material.Text

@Composable
fun NavBar(navController: NavHostController) {
    var selectedItem = 0
    val items = listOf(
        NavItem.Teams,
        NavItem.Profil,
        NavItem.Matches
    )

    BottomNavigation(
        contentColor = Color.White,
        backgroundColor = Color.Black,
        )
    {
        items.forEachIndexed{ index, item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.title) },
                selected = selectedItem == index,
                selectedContentColor = Color.White,
                onClick = { selectedItem = index
                    navController.navigate(item.route)
                }
            )
        }
    }
}