package com.mountplanner.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MountPlannerNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        Screen.Home to Pair("Inicio", Icons.Filled.Home),
        Screen.Expeditions to Pair("Expediciones", Icons.Filled.List),
        Screen.Map to Pair("Mapa", Icons.Filled.Map),
        Screen.Notebook to Pair("Cuaderno", Icons.Filled.Book),
        Screen.Pois to Pair("POIs", Icons.Filled.Place)
    )

    val showBottomBar = currentDestination?.route in listOf(
        Screen.Home.route,
        Screen.Expeditions.route,
        Screen.Map.route,
        Screen.Notebook.route,
        Screen.Pois.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { (screen, info) ->
                        val (label, icon) = info
                        NavigationBarItem(
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(if (screen == Screen.Map) Screen.Map.createRoute() else screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            composable(Screen.Home.route) {
                // HomeViewModel here
                Text("Home Screen")
            }
            composable(Screen.Expeditions.route) {
                Text("Expeditions Screen")
            }
            composable(Screen.Map.route) {
                Text("Map Screen")
            }
            composable(Screen.Notebook.route) {
                Text("Notebook Screen")
            }
            composable(Screen.Pois.route) {
                Text("Pois Screen")
            }
            composable(Screen.CreateExpedition.route) {
                Text("CreateExpedition")
            }
            composable(Screen.ExpeditionDetail.route) {
                Text("ExpeditionDetail")
            }
            composable(Screen.ActiveExpedition.route) {
                Text("ActiveExpedition")
            }
            composable(Screen.PoiDetail.route) {
                Text("PoiDetail")
            }
            composable(Screen.AddPoi.route) {
                Text("AddPoi")
            }
            composable(Screen.NoteDetail.route) {
                Text("NoteDetail")
            }
            composable(Screen.AddNote.route) {
                Text("AddNote")
            }
            composable(Screen.Weather.route) {
                Text("Weather")
            }
            composable(Screen.Checklist.route) {
                Text("Checklist")
            }
            composable(Screen.ShareLocation.route) {
                Text("ShareLocation")
            }
            composable(Screen.OfflineMap.route) {
                Text("OfflineMap")
            }
            composable(Screen.Settings.route) {
                Text("Settings")
            }
        }
    }
}
