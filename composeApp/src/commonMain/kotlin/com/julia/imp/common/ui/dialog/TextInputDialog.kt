package com.julia.imp.common.ui.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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

@Composable
fun TextInputDialog(
    title: String,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier,
    initialValue: String = "",
    placeholder: String? = null
) {
    val inputFocusRequester = remember { FocusRequester() }

    var inputState by remember {
        mutableStateOf(
            TextFieldValue(
                text = initialValue,
                selection = TextRange(initialValue.length)
            )
        )
    }

    BaseDialog(
        title = title,
        onDismissRequest = onDismissRequest,
        onConfirm = { onConfirm(inputState.text) },
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().focusRequester(inputFocusRequester),
            value = inputState,
            onValueChange = { inputState = it },
            singleLine = true,
            placeholder = { placeholder?.let { Text(it) }}
        )
    }

    LaunchedEffect(Unit) {
        inputFocusRequester.requestFocus()
    }
}