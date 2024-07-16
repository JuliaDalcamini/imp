package com.julia.imp.common.ui.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    icon: @Composable () -> Unit = { Icon(Icons.Outlined.Warning, null) }
) {
    ConfirmationDialog(
        icon = icon,
        title = title,
        message = message,
        onDismissRequest = onDismissRequest,
        onConfirm = {},
        showDismissButton = false
    )
}