package com.julia.imp.common.ui.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.cancel_label
import imp.composeapp.generated.resources.ok_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    icon: @Composable () -> Unit = { Icon(Icons.Outlined.Warning, null) }
) {
    AlertDialog(
        icon = icon,
        title = { Text(text = title) },
        text = { Text(text = message) },
        onDismissRequest = onDismissRequest,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.cancel_label))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismissRequest()
                }
            ) {
                Text(stringResource(Res.string.ok_label))
            }
        }
    )
}