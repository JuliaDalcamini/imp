package com.julia.imp.common.text

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