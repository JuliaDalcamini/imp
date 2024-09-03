package com.julia.imp.artifact

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
import com.julia.imp.user.User
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.artifact_external_link_label
import imp.composeapp.generated.resources.artifact_name_label
import imp.composeapp.generated.resources.artifact_type_label
import imp.composeapp.generated.resources.inspectors_label
import imp.composeapp.generated.resources.select_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArtifactFormFields(
    name: String,
    onNameChange: (String) -> Unit,
    type: ArtifactType?,
    onTypeChange: (ArtifactType) -> Unit,
    inspectors: List<User>,
    onAddInspectorClick: () -> Unit,
    onRemoveInspectorClick: (User) -> Unit,
    availableTypes: List<ArtifactType>?,
    externalLink: String,
    onExternalLinkChange: (String) -> Unit,
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

        OutlinedTextField(
            modifier = Modifier,
            value = externalLink,
            onValueChange = onExternalLinkChange,
            enabled = enabled,
            label = { Text(stringResource(Res.string.artifact_external_link_label)) },
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

        Spacer(Modifier.height(24.dp))
    }
}

