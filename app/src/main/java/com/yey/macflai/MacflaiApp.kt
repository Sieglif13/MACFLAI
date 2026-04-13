package com.yey.macflai

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.yey.macflai.worker.CleanupWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MacflaiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupWorkManager()
    }

    private fun setupWorkManager() {
        val constraints = Constraints.Builder()
            .setRequiresDeviceIdle(true)
            .setRequiresBatteryNotLow(true)
            .build()
            
        val periodicWorkRequest = PeriodicWorkRequestBuilder<CleanupWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "macflai_cleanup_work",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
}
