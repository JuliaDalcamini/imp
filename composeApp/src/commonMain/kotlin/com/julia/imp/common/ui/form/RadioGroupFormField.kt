package com.julia.imp.common.ui.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.julia.imp.common.ui.radio.LabeledRadioButton

@Composable
fun <T> RadioGroupFormField(
    label: String,
    options: List<T>,
    selectedOption: T?,
    onSelectionChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    optionLabel: @Composable (T) -> Unit = { Text(it.toString()) }
) {
    FormField(
        modifier = modifier,
        label = label
    ) {
        Column(Modifier.fillMaxWidth()) {
            options.forEach { option ->
                LabeledRadioButton(
                    modifier = Modifier.fillMaxWidth(),
                    selected = option == selectedOption,
                    onClick = { onSelectionChange(option) },
                    label = { optionLabel(option) },
                    enabled = enabled
                )
            }
        }
    }
}