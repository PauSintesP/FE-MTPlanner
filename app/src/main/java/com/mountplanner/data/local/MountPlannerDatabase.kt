package com.mountplanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mountplanner.data.local.dao.*
import com.mountplanner.data.local.entity.ChecklistItemEntity
import com.mountplanner.data.local.entity.ExpeditionEntity
import com.mountplanner.data.local.entity.LocationLogEntity
import com.mountplanner.data.local.entity.NoteEntity
import com.mountplanner.data.local.entity.PoiEntity
import com.mountplanner.data.local.entity.WeatherCacheEntity

@Database(
    entities = [
        ExpeditionEntity::class,
        PoiEntity::class,
        NoteEntity::class,
        ChecklistItemEntity::class,
        LocationLogEntity::class,
        WeatherCacheEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MountPlannerDatabase : RoomDatabase() {
    abstract fun expeditionDao(): ExpeditionDao
    abstract fun poiDao(): PoiDao
    abstract fun noteDao(): NoteDao
    abstract fun checklistDao(): ChecklistDao
    abstract fun locationLogDao(): LocationLogDao
    abstract fun weatherCacheDao(): WeatherCacheDao
}
