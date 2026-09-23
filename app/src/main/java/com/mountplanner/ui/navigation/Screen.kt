package com.mountplanner.ui.navigation

sealed class Screen(val route: String) {
    // Bottom Nav
    object Home : Screen("home")
    object Expeditions : Screen("expeditions")
    object Map : Screen("map/{expeditionId}") {
        fun createRoute(expeditionId: String = "global") = "map/$expeditionId"
    }
    object Notebook : Screen("notebook")
    object Pois : Screen("pois")
    
    // Expeditions
    object CreateExpedition : Screen("expedition/create")
    object ExpeditionDetail : Screen("expedition/{expeditionId}") {
        fun createRoute(id: String) = "expedition/$id"
    }
    object ActiveExpedition : Screen("expedition/active")
    
    // POIs
    object PoiDetail : Screen("poi/{poiId}") {
        fun createRoute(id: String) = "poi/$id"
    }
    object AddPoi : Screen("poi/add?lat={lat}&lng={lng}") {
        fun createRoute(lat: Double? = null, lng: Double? = null) = 
            if (lat != null && lng != null) "poi/add?lat=$lat&lng=$lng" else "poi/add"
    }
    
    // Notes
    object NoteDetail : Screen("note/{noteId}") {
        fun createRoute(id: String) = "note/$id"
    }
    object AddNote : Screen("note/add?expeditionId={expeditionId}") {
        fun createRoute(expeditionId: String? = null) =
            if (expeditionId != null) "note/add?expeditionId=$expeditionId" else "note/add"
    }
    
    // Other
    object Weather : Screen("weather/{expeditionId}") {
        fun createRoute(id: String) = "weather/$id"
    }
    object Checklist : Screen("checklist/{expeditionId}") {
        fun createRoute(id: String) = "checklist/$id"
    }
    object ShareLocation : Screen("share/{expeditionId}") {
        fun createRoute(id: String) = "share/$id"
    }
    object OfflineMap : Screen("map/offline")
    object Settings : Screen("settings")
}
