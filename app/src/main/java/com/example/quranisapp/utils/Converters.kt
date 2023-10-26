package com.example.quranisapp.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.example.quranisapp.data.database.Juz

object Converters {
    fun List<Juz>.mapToJuzIndexing(): List<JuzWithSurahIndex>{
        val groupedList = this.groupBy { it.juzNumber }
        return groupedList.map { (juzNumber, juzList)->
            JuzWithSurahIndex(
                juzNumber,
                juzList.map { "${it.surahNameEn} | ${it.surahNameAr}" },
                juzList.map { it.surahNumber }
            )
        }
    }
    data class JuzWithSurahIndex(
        val juzNumber: Int?,
        val surahList: List<String?>,
        val surahNumberList: List<Int?>
    )

    fun convertNumberToThreeDigits(
        number: Int
    ): String{
        return String.format("%03d", number)//format 3 digit belakangnnya nol
    }

    fun Spannable.toAnnotatedString(primaryColor: Color): AnnotatedString {
        val builder = AnnotatedString.Builder(this.toString())
        val copierContext = CopierContext(primaryColor)
        SpanCopier.values().forEach { copier ->
            getSpans(0, length, copier.spanClass).forEach { span ->
                copier.copySpan(span, getSpanStart(span), getSpanEnd(span), builder, copierContext)
            }
        }
        return builder.toAnnotatedString()
    }

    private data class CopierContext(
        val primaryColor: Color,
    )

    private enum class SpanCopier {
        URL {
            override val spanClass = URLSpan::class.java
            override fun copySpan(
                span: Any,
                start: Int,
                end: Int,
                destination: AnnotatedString.Builder,
                context: CopierContext
            ) {
                val urlSpan = span as URLSpan
                destination.addStringAnnotation(
                    tag = name,
                    annotation = urlSpan.url,
                    start = start,
                    end = end,
                )
                destination.addStyle(
                    style = SpanStyle(color = context.primaryColor, textDecoration = TextDecoration.Underline),
                    start = start,
                    end = end,
                )
            }
        },
        FOREGROUND_COLOR {
            override val spanClass = ForegroundColorSpan::class.java
            override fun copySpan(
                span: Any,
                start: Int,
                end: Int,
                destination: AnnotatedString.Builder,
                context: CopierContext
            ) {
                val colorSpan = span as ForegroundColorSpan
                destination.addStyle(
                    style = SpanStyle(color = Color(colorSpan.foregroundColor)),
                    start = start,
                    end = end,
                )
            }
        },
        UNDERLINE {
            override val spanClass = UnderlineSpan::class.java
            override fun copySpan(
                span: Any,
                start: Int,
                end: Int,
                destination: AnnotatedString.Builder,
                context: CopierContext
            ) {
                destination.addStyle(
                    style = SpanStyle(textDecoration = TextDecoration.Underline),
                    start = start,
                    end = end,
                )
            }
        },
        STYLE {
            override val spanClass = StyleSpan::class.java
            override fun copySpan(
                span: Any,
                start: Int,
                end: Int,
                destination: AnnotatedString.Builder,
                context: CopierContext
            ) {
                val styleSpan = span as StyleSpan

                destination.addStyle(
                    style = when (styleSpan.style) {
                        Typeface.ITALIC -> SpanStyle(fontStyle = FontStyle.Italic)
                        Typeface.BOLD -> SpanStyle(fontWeight = FontWeight.Bold)
                        Typeface.BOLD_ITALIC -> SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
                        else -> SpanStyle()
                    },
                    start = start,
                    end = end,
                )
            }
        };

        abstract val spanClass: Class<out CharacterStyle>
        abstract fun copySpan(span: Any, start: Int, end: Int, destination: AnnotatedString.Builder, context: CopierContext)
    }
}
