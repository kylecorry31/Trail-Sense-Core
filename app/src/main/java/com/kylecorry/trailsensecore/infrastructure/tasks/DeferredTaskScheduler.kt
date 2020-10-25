package com.kylecorry.trailsensecore.infrastructure.tasks

import android.content.Context
import androidx.work.*
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeUnit

class DeferredTaskScheduler(
    private val context: Context,
    private val task: Class<out ListenableWorker>,
    private val uniqueId: String,
    private val constraints: Constraints? = null
) : ITaskScheduler {


    override fun schedule(delay: Duration) {
        val workManager = WorkManager.getInstance(context.applicationContext)

        val request = OneTimeWorkRequest
            .Builder(task).apply {
                addTag(uniqueId)
                setInitialDelay(delay.toMillis(), TimeUnit.MILLISECONDS)
                if (constraints != null) {
                    setConstraints(constraints)
                }
            }
            .build()

        workManager.enqueueUniqueWork(
            uniqueId,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    override fun schedule(time: Instant) {
        schedule(Duration.between(Instant.now(), time))
    }

    override fun cancel() {
        val workManager = WorkManager.getInstance(context.applicationContext)
        workManager.cancelUniqueWork(uniqueId)
    }
}