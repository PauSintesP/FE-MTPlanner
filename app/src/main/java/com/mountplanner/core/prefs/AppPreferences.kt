package com.mountplanner.core.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mountplanner_prefs")

@Singleton
class AppPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val ACTIVE_EXPEDITION_ID = stringPreferencesKey("active_expedition_id")
        val MOUNT_REPORTER_TRIP_ID = stringPreferencesKey("mount_reporter_trip_id")
        val MOUNT_REPORTER_SHARE_TOKEN = stringPreferencesKey("mount_reporter_share_token")
        val MOUNT_REPORTER_ENABLED = booleanPreferencesKey("mount_reporter_enabled")
        val PING_INTERVAL_MINUTES = longPreferencesKey("ping_interval_minutes")
        val TRIP_STATUS = stringPreferencesKey("trip_status")
        val LAST_PING_LAT = doublePreferencesKey("last_ping_lat")
        val LAST_PING_LNG = doublePreferencesKey("last_ping_lng")
        val LAST_PING_TS = longPreferencesKey("last_ping_ts")
        val USER_NAME = stringPreferencesKey("user_name")
        val DISTANCE_UNIT = stringPreferencesKey("distance_unit")
        val ALTITUDE_UNIT = stringPreferencesKey("altitude_unit")
        val TEMPERATURE_UNIT = stringPreferencesKey("temperature_unit")
    }

    val activeExpeditionId: Flow<String?> = dataStore.data.map { it[ACTIVE_EXPEDITION_ID] }
    val mountReporterTripId: Flow<String?> = dataStore.data.map { it[MOUNT_REPORTER_TRIP_ID] }
    val mountReporterShareToken: Flow<String?> = dataStore.data.map { it[MOUNT_REPORTER_SHARE_TOKEN] }
    val mountReporterEnabled: Flow<Boolean> = dataStore.data.map { it[MOUNT_REPORTER_ENABLED] ?: false }
    val pingIntervalMinutes: Flow<Long> = dataStore.data.map { it[PING_INTERVAL_MINUTES] ?: 30L }
    val tripStatus: Flow<String> = dataStore.data.map { it[TRIP_STATUS] ?: "none" }
    val lastPingLat: Flow<Double?> = dataStore.data.map { it[LAST_PING_LAT] }
    val lastPingLng: Flow<Double?> = dataStore.data.map { it[LAST_PING_LNG] }
    val lastPingTs: Flow<Long?> = dataStore.data.map { it[LAST_PING_TS] }
    val userName: Flow<String> = dataStore.data.map { it[USER_NAME] ?: "" }
    val distanceUnit: Flow<String> = dataStore.data.map { it[DISTANCE_UNIT] ?: "km" }
    val altitudeUnit: Flow<String> = dataStore.data.map { it[ALTITUDE_UNIT] ?: "m" }
    val temperatureUnit: Flow<String> = dataStore.data.map { it[TEMPERATURE_UNIT] ?: "C" }

    suspend fun setActiveExpeditionId(id: String?) {
        dataStore.edit { prefs ->
            if (id == null) prefs.remove(ACTIVE_EXPEDITION_ID) else prefs[ACTIVE_EXPEDITION_ID] = id
        }
    }

    suspend fun setMountReporterTripId(id: String?) {
        dataStore.edit { prefs ->
            if (id == null) prefs.remove(MOUNT_REPORTER_TRIP_ID) else prefs[MOUNT_REPORTER_TRIP_ID] = id
        }
    }

    suspend fun setMountReporterShareToken(token: String?) {
        dataStore.edit { prefs ->
            if (token == null) prefs.remove(MOUNT_REPORTER_SHARE_TOKEN) else prefs[MOUNT_REPORTER_SHARE_TOKEN] = token
        }
    }

    suspend fun setMountReporterEnabled(enabled: Boolean) {
        dataStore.edit { it[MOUNT_REPORTER_ENABLED] = enabled }
    }

    suspend fun setPingIntervalMinutes(minutes: Long) {
        dataStore.edit { it[PING_INTERVAL_MINUTES] = minutes }
    }

    suspend fun setTripStatus(status: String) {
        dataStore.edit { it[TRIP_STATUS] = status }
    }

    suspend fun setLastPingLocation(lat: Double, lng: Double, ts: Long) {
        dataStore.edit { prefs ->
            prefs[LAST_PING_LAT] = lat
            prefs[LAST_PING_LNG] = lng
            prefs[LAST_PING_TS] = ts
        }
    }

    suspend fun setUserName(name: String) {
        dataStore.edit { it[USER_NAME] = name }
    }

    suspend fun setUnits(distance: String, altitude: String, temperature: String) {
        dataStore.edit { prefs ->
            prefs[DISTANCE_UNIT] = distance
            prefs[ALTITUDE_UNIT] = altitude
            prefs[TEMPERATURE_UNIT] = temperature
        }
    }
}
