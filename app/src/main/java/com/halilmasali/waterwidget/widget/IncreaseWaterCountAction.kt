package com.halilmasali.waterwidget.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState

class IncreaseWaterCountAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            val currentCount = prefs[WaterTrackerWidget.countKey]
            if(currentCount != null) {
                prefs[WaterTrackerWidget.countKey] = currentCount + 1
            } else {
                prefs[WaterTrackerWidget.countKey] = 1
            }
        }
        WaterTrackerWidget.update(context, glanceId)
    }
}