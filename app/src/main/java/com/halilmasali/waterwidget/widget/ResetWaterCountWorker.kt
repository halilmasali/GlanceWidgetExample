package com.halilmasali.waterwidget.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ResetWaterCountWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val glanceManager = GlanceAppWidgetManager(applicationContext)
        val glanceIds = glanceManager.getGlanceIds(WaterTrackerWidget::class.java)

        glanceIds.forEach { glanceId ->
            updateAppWidgetState(applicationContext, glanceId) { prefs ->
                prefs[WaterTrackerWidget.countKey] = 0
            }
            WaterTrackerWidget.update(applicationContext, glanceId)
        }

        return Result.success()
    }
}