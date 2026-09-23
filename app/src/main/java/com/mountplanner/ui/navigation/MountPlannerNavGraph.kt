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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mountplanner.ui.checklist.ChecklistScreen
import com.mountplanner.ui.expedition.active.ActiveExpeditionScreen
import com.mountplanner.ui.expedition.create.CreateExpeditionScreen
import com.mountplanner.ui.expedition.detail.ExpeditionDetailScreen
import com.mountplanner.ui.expedition.list.ExpeditionListScreen
import com.mountplanner.ui.home.HomeScreen
import com.mountplanner.ui.map.OfflineMapScreen
import com.mountplanner.ui.map.PlanningMapScreen
import com.mountplanner.ui.notebook.AddNoteScreen
import com.mountplanner.ui.notebook.NotebookScreen
import com.mountplanner.ui.poi.AddPoiScreen
import com.mountplanner.ui.poi.PoiListScreen
import com.mountplanner.ui.settings.SettingsScreen
import com.mountplanner.ui.sharing.ShareLocationScreen
import com.mountplanner.ui.weather.WeatherScreen

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
                HomeScreen(
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                    onNavigateToCreateExpedition = { navController.navigate(Screen.CreateExpedition.route) },
                    onNavigateToActiveExpedition = { navController.navigate(Screen.ActiveExpedition.route) }
                )
            }
            composable(Screen.Expeditions.route) {
                ExpeditionListScreen(
                    onNavigateToCreate = { navController.navigate(Screen.CreateExpedition.route) },
                    onNavigateToDetail = { id -> navController.navigate(Screen.ExpeditionDetail.createRoute(id)) }
                )
            }
            composable(Screen.Map.route) {
                PlanningMapScreen()
            }
            composable(Screen.Notebook.route) {
                NotebookScreen(
                    onAddNoteClick = { navController.navigate(Screen.AddNote.createRoute()) },
                    onNoteClick = { /* Note click */ }
                )
            }
            composable(Screen.Pois.route) {
                PoiListScreen(
                    onAddPoiClick = { navController.navigate(Screen.AddPoi.createRoute()) },
                    onPoiClick = { /* POI click */ }
                )
            }
            composable(Screen.CreateExpedition.route) {
                CreateExpeditionScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.ExpeditionDetail.route) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("expeditionId") ?: ""
                ExpeditionDetailScreen(
                    expeditionId = id,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.ActiveExpedition.route) {
                ActiveExpeditionScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.AddPoi.route) {
                AddPoiScreen(
                    onBackClick = { navController.popBackStack() },
                    onSaveSuccess = { navController.popBackStack() }
                )
            }
            composable(Screen.AddNote.route) {
                AddNoteScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.Weather.route) {
                WeatherScreen()
            }
            composable(Screen.Checklist.route) {
                ChecklistScreen()
            }
            composable(Screen.ShareLocation.route) {
                ShareLocationScreen()
            }
            composable(Screen.OfflineMap.route) {
                OfflineMapScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
