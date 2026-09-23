package com.mountplanner.core.di

import android.content.Context
import androidx.room.Room
import com.mountplanner.data.local.MountPlannerDatabase
import com.mountplanner.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): MountPlannerDatabase {
        return Room.databaseBuilder(
            ctx,
            MountPlannerDatabase::class.java,
            "mount_planner.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideExpeditionDao(db: MountPlannerDatabase): ExpeditionDao = db.expeditionDao()

    @Provides
    fun providePoiDao(db: MountPlannerDatabase): PoiDao = db.poiDao()

    @Provides
    fun provideNoteDao(db: MountPlannerDatabase): NoteDao = db.noteDao()

    @Provides
    fun provideChecklistDao(db: MountPlannerDatabase): ChecklistDao = db.checklistDao()

    @Provides
    fun provideLocationLogDao(db: MountPlannerDatabase): LocationLogDao = db.locationLogDao()

    @Provides
    fun provideWeatherCacheDao(db: MountPlannerDatabase): WeatherCacheDao = db.weatherCacheDao()
}
