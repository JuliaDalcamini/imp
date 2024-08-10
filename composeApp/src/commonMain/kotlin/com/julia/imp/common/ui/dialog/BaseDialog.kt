package com.julia.imp.common.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.cancel_label
import imp.composeapp.generated.resources.confirm_label
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BaseDialog(
    title: String,
    onConfirm: (() -> Unit)?,
    enableConfirm: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = RoundedCornerShape(28.0.dp),
            tonalElevation = 6.0.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = title,
                    style = MaterialTheme.typography.headlineSmall
                )

                content()

                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.End
                    )
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(stringResource(Res.string.cancel_label))
                    }

                    if (onConfirm != null) {
                        TextButton(
                            enabled = enableConfirm,
                            onClick = {
                                onConfirm()
                                onDismissRequest()
                            }
                        ) {
                            Text(stringResource(Res.string.confirm_label))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BaseDialog(
    title: String,
    onConfirm: (() -> Unit),
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    enableConfirm: Boolean = true,
    content: @Composable () -> Unit
) {
    BaseDialog(
        title = title,
        onConfirm = onConfirm,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        enableConfirm = enableConfirm,
        content = content
    )
}

@Composable
fun BaseDialog(
    title: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BaseDialog(
        title = title,
        onConfirm = null,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        enableConfirm = false,
        content = content
    )
}