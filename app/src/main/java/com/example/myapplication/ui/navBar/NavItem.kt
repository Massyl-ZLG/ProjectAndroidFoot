package com.example.myapplication.ui.navBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myapplication.ui.NavigationKeys

sealed class NavItem(var route: String, var icon: ImageVector, var title: String) {
    object Teams: NavItem(NavigationKeys.Route.TEAMS_LIST, Icons.Filled.Menu, "Equipe")
    object Profil: NavItem(NavigationKeys.Route.PROFILE, Icons.Filled.Person, "Profil")
    object Matches: NavItem(NavigationKeys.Route.MATCHES_LIST, Icons.Filled.Notifications, "Matches")
}