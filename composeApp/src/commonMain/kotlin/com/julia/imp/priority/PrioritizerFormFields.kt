package com.julia.imp.priority

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julia.imp.common.ui.form.DropdownFormField
import com.julia.imp.common.ui.form.SliderFormField
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.complexity_label
import imp.composeapp.generated.resources.impact_label
import imp.composeapp.generated.resources.priority_label
import imp.composeapp.generated.resources.user_value_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun PrioritizerFormFields(
    priority: Priority?,
    onPriorityChange: (Priority) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(modifier) {
        when (priority) {
            is MoscowPriority -> MoscowPriorityFormFields(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                priority = priority,
                onValueChange = onPriorityChange,
                enabled = enabled
            )

            is WiegersPriority -> WiegersPriorityFormFields(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                priority = priority,
                onValueChange = onPriorityChange,
                enabled = enabled
            )

            null -> {}
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun MoscowPriorityFormFields(
    priority: MoscowPriority,
    onValueChange: (MoscowPriority) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    DropdownFormField(
        modifier = modifier,
        text = priority.level.getLabel(),
        label = stringResource(Res.string.priority_label),
        options = MoscowPriorityLevel.entries,
        onOptionSelected = { level -> onValueChange(priority.copy(level = level)) },
        enabled = enabled,
        optionLabel = { level -> Text(level.getLabel()) }
    )
}

@Composable
private fun WiegersPriorityFormFields(
    priority: WiegersPriority,
    onValueChange: (WiegersPriority) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        PrioritySliderFormField(
            label = stringResource(Res.string.user_value_label),
            enabled = enabled,
            value = priority.userValue,
            onValueChange = { onValueChange(priority.copy(userValue = it)) }
        )

        PrioritySliderFormField(
            label = stringResource(Res.string.complexity_label),
            enabled = enabled,
            value = priority.complexity,
            onValueChange = { onValueChange(priority.copy(complexity = it)) }
        )

        PrioritySliderFormField(
            label = stringResource(Res.string.impact_label),
            enabled = enabled,
            value = priority.impact,
            onValueChange = { onValueChange(priority.copy(impact = it)) }
        )
    }
}

@Composable
private fun PrioritySliderFormField(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    SliderFormField(
        modifier = modifier,
        label = label,
        value = value.toFloat(),
        onValueChange = { onValueChange(it.toInt()) },
        enabled = enabled,
        steps = 4,
        valueRange = 1f..5f,
        valueText = value.toString()
    )
}