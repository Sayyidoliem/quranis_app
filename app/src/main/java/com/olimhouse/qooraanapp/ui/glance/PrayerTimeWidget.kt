package com.olimhouse.qooraanapp.ui.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.olimhouse.qooraanapp.data.kotpref.SettingPreferences

class PrayerTimeWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                PrayerTimeUIWidget()
            }
        }
    }

    @Composable
    private fun PrayerTimeUIWidget() {

        Column(
            modifier = GlanceModifier.fillMaxSize().appWidgetBackground()
                .background(GlanceTheme.colors.background).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = when (SettingPreferences.isSelectedLanguage) {
                    SettingPreferences.INDONESIA -> {
                        "Jadwal Ibadah"
                    }
                    else -> {
                        "Prayer Timings"
                    }
                },
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                maxLines =  1
            )
            Text(
                text = "Shubuh : ",
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp),
                maxLines = 1
            )
            Text(
                text = "Dzhuhur : ",
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp),
                maxLines = 1
            )
            Text(
                text = "Ashar : ",
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp),
                maxLines = 1
            )
            Text(
                text = "Maghrib : ",
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp),
                maxLines = 1
            )
            Text(
                text = "Isya : ",
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp),
                maxLines = 1
            )

        }
    }
}

class PrayerTimeWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PrayerTimeWidget()
}