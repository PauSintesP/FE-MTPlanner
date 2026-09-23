package com.mountplanner.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mountplanner.core.prefs.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import androidx.work.ListenableWorker // Placeholder for the actual LocationWorker class which is not created yet

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            CoroutineScope(Dispatchers.IO).launch {
                val activeExpeditionId = appPreferences.activeExpeditionId.first()
                val mountReporterEnabled = appPreferences.mountReporterEnabled.first()
                
                if (activeExpeditionId != null && mountReporterEnabled) {
                    // Re-schedule worker
                    // This assumes a LocationWorker exists, which would be scheduled like this:
                    /*
                    val workRequest = PeriodicWorkRequestBuilder<LocationWorker>(
                        15, TimeUnit.MINUTES
                    ).build()
                    
                    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                        "LocationWorker",
                        ExistingPeriodicWorkPolicy.KEEP,
                        workRequest
                    )
                    */
                }
            }
        }
    }
}
