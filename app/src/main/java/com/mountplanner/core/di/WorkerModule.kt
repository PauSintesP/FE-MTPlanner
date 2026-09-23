package com.mountplanner.core.di

import androidx.hilt.work.HiltWorkerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {

    // HiltWorkerFactory is available automatically when hilt-work is included.
    // However, if we need to provide something specific for Workers, we add it here.
}
