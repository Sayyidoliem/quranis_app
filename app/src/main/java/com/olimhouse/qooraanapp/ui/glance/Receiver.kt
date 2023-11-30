package com.olimhouse.qooraanapp.ui.glance

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

object Receiver {

    class PrayerTimeWidgetReceiver : GlanceAppWidgetReceiver() {
        override val glanceAppWidget: GlanceAppWidget = PrayerTimeWidget()
    }

    class LastReadWidgetReceiver : GlanceAppWidgetReceiver() {
        override val glanceAppWidget: GlanceAppWidget = LastReadWidget()
    }
}