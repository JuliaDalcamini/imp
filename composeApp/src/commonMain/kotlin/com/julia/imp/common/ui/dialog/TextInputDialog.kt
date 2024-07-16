package com.julia.imp.common.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.cancel_label
import imp.composeapp.generated.resources.rename_label
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInputDialog(
    title: String,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier,
    initialValue: String = "",
    placeholder: String? = null
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = RoundedCornerShape(28.0.dp),
            tonalElevation = 6.0.dp
        ) {
            Column(Modifier.fillMaxWidth().padding(24.dp)) {
                val inputFocusRequester = remember { FocusRequester() }

                var inputState by remember {
                    mutableStateOf(
                        TextFieldValue(
                            text = initialValue,
                            selection = TextRange(initialValue.length)
                        )
                    )
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().focusRequester(inputFocusRequester),
                    value = inputState,
                    onValueChange = { inputState = it },
                    singleLine = true,
                    placeholder = { placeholder?.let { Text(it) }}
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(stringResource(Res.string.cancel_label))
                    }

                    Spacer(Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            onConfirm(inputState.text)
                            onDismissRequest()
                        }
                    ) {
                        Text(stringResource(Res.string.rename_label))
                    }
                }

                LaunchedEffect(Unit) {
                    inputFocusRequester.requestFocus()
                }
            }
        }
    }
}