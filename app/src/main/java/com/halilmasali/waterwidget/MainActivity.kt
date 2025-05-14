package com.halilmasali.waterwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.halilmasali.waterwidget.ui.theme.WaterWidgetTheme
import com.halilmasali.waterwidget.widget.ResetWaterCountWorker
import com.halilmasali.waterwidget.widget.WaterTackerWidgetReceiver
import com.halilmasali.waterwidget.widget.WaterTrackerWidget
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        scheduleDailyReset()
        setContent {
            WaterWidgetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Add Widget to your home screen",
                            modifier = Modifier.padding(16.dp),
                        )
                        Button(
                            onClick = { addWidget() }
                        ) {
                            Text(text = "Add Widget")
                        }
                    }
                }
            }
        }

    }

    private fun addWidget() {
        val widgetManager = AppWidgetManager.getInstance(applicationContext)
        val widgetComponent =
            ComponentName(applicationContext, WaterTackerWidgetReceiver::class.java)

        if (widgetManager.isRequestPinAppWidgetSupported) {
            val pinnedWidgetCallbackIntent =
                Intent(applicationContext, WaterTackerWidgetReceiver::class.java)
            val successCallback = PendingIntent.getBroadcast(
                applicationContext,
                0,
                pinnedWidgetCallbackIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            // Create the intent to request the widget to be pinned
            widgetManager.requestPinAppWidget(widgetComponent, null, successCallback)
            return
        }
    }

    private fun scheduleDailyReset() {
        val resetWorkRequest = PeriodicWorkRequestBuilder<ResetWaterCountWorker>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "DailyResetWork",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            resetWorkRequest
        )
    }
}
