package com.halilmasali.waterwidget.widget

import com.halilmasali.waterwidget.R
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentWidth
import androidx.glance.text.Text
import androidx.glance.text.TextStyle


object WaterTrackerWidget : GlanceAppWidget() {
    val countKey = intPreferencesKey("water_count")
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            MyContent()
        }
    }


    @Composable
    private fun MyContent() {
        val count = currentState(key = countKey) ?: 0
        val target = 10 // Default value
        val progress = count / target.toFloat()

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(8.dp)
                // Add a background for rounded corners all Android versions
                .background(ImageProvider(R.drawable.rounded_background)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = GlanceModifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                LinearProgressIndicator(
                    modifier = GlanceModifier.fillMaxWidth().height(30.dp),
                    progress = progress,
                    color = ColorProvider(day = Color(0xFF29B6F6), night = Color(0xFF29B6F6)),
                    backgroundColor = ColorProvider(Color(0xFFE0E0E0), Color(0xFFBDBDBD))
                )
                Text(
                    text = "$count / $target",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = ColorProvider(Color(0xFF000000), Color(0xFFFFFFFF))
                    ),
                    modifier = GlanceModifier
                        .wrapContentWidth()
                        .padding(8.dp)
                )
            }

            Spacer(GlanceModifier.height(8.dp))
            Row {
                CircleIconButton(
                    imageProvider = ImageProvider(resId = R.drawable.baseline_remove_24),
                    contentDescription = "Remove",
                    onClick = actionRunCallback(DecreaseWaterCountAction::class.java),
                    modifier = GlanceModifier.size(35.dp)
                )
                Spacer(GlanceModifier.width(8.dp))
                CircleIconButton(
                    imageProvider = ImageProvider(resId = R.drawable.baseline_add_24),
                    contentDescription = "Add",
                    onClick = actionRunCallback(IncreaseWaterCountAction::class.java),
                    modifier = GlanceModifier.size(35.dp)
                )
            }

        }
    }

}
