package com.julia.imp.artifact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julia.imp.common.ui.form.DropdownFormField
import com.julia.imp.common.ui.form.SliderFormField
import com.julia.imp.priority.MoscowPriority
import com.julia.imp.priority.MoscowPriorityLevel
import com.julia.imp.priority.Priority
import com.julia.imp.priority.WiegersPriority
import com.julia.imp.user.User
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.artifact_name_label
import imp.composeapp.generated.resources.artifact_type_label
import imp.composeapp.generated.resources.complexity_label
import imp.composeapp.generated.resources.impact_label
import imp.composeapp.generated.resources.inspectors_label
import imp.composeapp.generated.resources.priority_label
import imp.composeapp.generated.resources.select_label
import imp.composeapp.generated.resources.user_value_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArtifactFormFields(
    name: String,
    onNameChange: (String) -> Unit,
    type: ArtifactType?,
    onTypeChange: (ArtifactType) -> Unit,
    priority: Priority?,
    onPriorityChange: (Priority) -> Unit,
    inspectors: List<User>,
    onAddInspectorClick: () -> Unit,
    onRemoveInspectorClick: (User) -> Unit,
    availableTypes: List<ArtifactType>?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = onNameChange,
            enabled = enabled,
            label = { Text(stringResource(Res.string.artifact_name_label)) },
            singleLine = true
        )

        if (availableTypes != null) {
            DropdownFormField(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                text = type?.name ?: stringResource(Res.string.select_label),
                label = stringResource(Res.string.artifact_type_label),
                options = availableTypes,
                onOptionSelected = onTypeChange,
                enabled = enabled,
                optionLabel = { optionType -> Text(optionType.name) }
            )
        }

        Text(
            modifier = Modifier.padding(top = 24.dp, bottom = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            text = stringResource(Res.string.inspectors_label)
        )

        ArtifactInspectorList(
            modifier = Modifier.fillMaxWidth(),
            inspectors = inspectors,
            onAddClick = onAddInspectorClick,
            onRemoveClick = onRemoveInspectorClick,
            enabled = enabled
        )

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
        steps = 8,
        valueRange = 1f..10f,
        valueText = value.toString()
    )
}