package com.julia.imp.common.ui.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julia.imp.common.ui.radio.LabeledRadioButton

@Composable
fun <T> SelectionDialog(
    title: String,
    options: List<T>,
    onDismissRequest: () -> Unit,
    onConfirm: (T) -> Unit,
    modifier: Modifier = Modifier,
    initialSelection: T? = null,
    optionLabel: @Composable (T) -> Unit = { Text(it.toString()) },
    message: String? = null
) {
    var selectedOption by remember { mutableStateOf(initialSelection) }

    BaseDialog(
        modifier = modifier,
        title = title,
        onDismissRequest = onDismissRequest,
        onConfirm = { selectedOption?.let { onConfirm(it) } },
        enableConfirm = selectedOption != null
    ) {
        message?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        LazyColumn {
            items(options) { option ->
                LabeledRadioButton(
                    modifier = Modifier.fillMaxWidth(),
                    selected = option == selectedOption,
                    onClick = { selectedOption = option },
                    label = { optionLabel(option) }
                )
            }
        }
    }
}