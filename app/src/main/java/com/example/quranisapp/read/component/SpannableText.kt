package com.example.quranisapp.read.component

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle

@Composable
fun SpannableText(
    text: String,
    modifier: Modifier = Modifier,
    spanStyle: SpanStyle = SpanStyle(color = MaterialTheme.colorScheme.primary),
    style: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onBackground
    ),
    overflow: TextOverflow = TextOverflow.Clip,
    softWarp: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    onClick: (String) -> Unit
) {
    val fixedText = Regex("(?<=\\p{L})(?=\\d)").replace(text, " ")
    val annotatedString = buildSpannable(fixedText, spanStyle)
    ClickableText(
        text = annotatedString,
        modifier,
        style,
        softWarp,
        overflow,
        maxLines,
        onTextLayout,
        onClick = { offset ->
            fixedText.split(" ").forEach { tag ->
                annotatedString.getStringAnnotations(tag, offset, offset).firstOrNull()?.let {
                    onClick.invoke(it.item)
                }
            }
        },
    )
}


private fun buildSpannable(
    text: String,
    spanStyle: SpanStyle
) = buildAnnotatedString {
    text.split(" ").forEach {//split buat misahin text per spasi
        if ("""\d""".toRegex().containsMatchIn(it)) {
            pushStringAnnotation(it, it)
            withStyle(
                style = spanStyle.copy(
                    textDecoration = TextDecoration.Underline,
                )
            ) {
                append("$it ")//tambahin spasi di akhiran
            }
            pop()//
        } else {
            append("$it ")
        }
    }
}