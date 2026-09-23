package com.mountplanner

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import org.osmdroid.config.Configuration as OsmConfig
import javax.inject.Inject

@HiltAndroidApp
class MountPlannerApplication : Application(), Configuration.Provider {
    
    @Inject lateinit var workerFactory: androidx.hilt.work.HiltWorkerFactory
    
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    
    override fun onCreate() {
        super.onCreate()
        // Inicializar Osmdroid
        OsmConfig.getInstance().apply {
            load(this@MountPlannerApplication, 
                getSharedPreferences("osmdroid", MODE_PRIVATE))
            userAgentValue = "MountPlanner/1.0 (Android)"
        }
    }
}
