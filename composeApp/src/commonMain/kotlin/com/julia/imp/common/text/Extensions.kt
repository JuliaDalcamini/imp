package com.julia.imp.common.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle

/**
 * Get the first and last initials of a string.
 */
fun String.getInitials(): String {
    val initials = this.uppercase().split(' ').mapNotNull { it.firstOrNull()?.toString() }

    return when {
        initials.size > 1 -> initials.first() + initials.last()
        initials.size == 1 -> initials.single()
        else -> ""
    }
}

/**
 * Convert a string to a link AnnotatedString.
 */
@Composable
fun String.toLink(url: String = this) = buildAnnotatedString {
    withStyle(
        SpanStyle(
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
        )
    ) {
        withLink(LinkAnnotation.Url(url)) {
            append(url)
        }
    }
}