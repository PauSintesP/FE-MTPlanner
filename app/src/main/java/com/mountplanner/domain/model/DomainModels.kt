package com.mountplanner.domain.model

typealias Expedition = com.mountplanner.data.model.Expedition
typealias Poi = com.mountplanner.data.model.Poi
typealias PoiCategory = com.mountplanner.data.model.PoiCategory
typealias Note = com.mountplanner.data.model.Note
typealias ChecklistItem = com.mountplanner.data.model.ChecklistItem
typealias LocationPoint = com.mountplanner.data.model.LocationPoint
typealias WeatherData = com.mountplanner.data.model.WeatherData
typealias PingRequest = com.mountplanner.data.remote.PingRequest
typealias CreateTripRequest = com.mountplanner.data.remote.CreateTripRequest
typealias TripResponse = com.mountplanner.data.remote.TripResponse
typealias CampRequest = com.mountplanner.data.remote.CampRequest
typealias ResumeRequest = com.mountplanner.data.remote.ResumeRequest
typealias StatsResponse = com.mountplanner.data.remote.StatsResponse
typealias PingsResponse = com.mountplanner.data.remote.PingsResponse

val Note.moodEmoji: String get() = mood ?: "📝"
val Note.weatherEmoji: String get() = weather ?: "☀️"
val Note.timestamp: Long get() = createdAt
