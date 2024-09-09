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
 * Format a double to a string with a fixed number of decimal places.
 */
fun Number.format(decimalPlaces: Int = 2): String {
    val string = this.toString()
    val (wholePart, decimalPart) = string.split('.')

    return "$wholePart.${decimalPart.take(decimalPlaces)}"
}

/**
 * Format a number as a currency string.
 */
fun Number.formatAsCurrency(): String {
    val string = this.toString()
    val (wholePart, decimalPart) = string.split('.')

    val formattedWholePart = wholePart
        .reversed()
        .chunked(3)
        .joinToString(".")
        .reversed()

    val formattedDecimalPart = decimalPart.take(2)

    return "R$ $formattedWholePart,$formattedDecimalPart"
}

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