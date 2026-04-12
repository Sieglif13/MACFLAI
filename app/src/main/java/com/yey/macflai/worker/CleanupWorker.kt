package com.yey.macflai.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yey.macflai.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CleanupWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val dao = AppDatabase.getDatabase(applicationContext).macflaiDao()
                // 10 dias en milisegundos: 10 * 24 * 60 * 60 * 1000 = 864000000
                val tenDaysAgo = System.currentTimeMillis() - 864_000_000L
                dao.cleanupOldDesafios(tenDaysAgo)
                Result.success()
            } catch (e: Exception) {
                Result.retry()
            }
        }
    }
}
