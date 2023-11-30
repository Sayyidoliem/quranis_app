package com.olimhouse.qooraanapp.ui.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.text.Text

class LastReadWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                LastReadUIWidget()
            }
        }
    }

    @Composable
    private fun LastReadUIWidget() {
        Text(text = "preketek")

    }
}