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
import imp.composeapp.generated.resources.artifact_version_label
import imp.composeapp.generated.resources.inspectors_label
import imp.composeapp.generated.resources.select_label
import imp.composeapp.generated.resources.version_change_needed_message
import org.jetbrains.compose.resources.stringResource

@Composable
private fun ArtifactFormFields(
    name: String,
    onNameChange: (String) -> Unit,
    currentVersion: String,
    onVersionChange: (String) -> Unit,
    needsVersionChange: Boolean,
    showType: Boolean,
    type: ArtifactType?,
    onTypeChange: ((ArtifactType) -> Unit)?,
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
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            value = currentVersion,
            onValueChange = onVersionChange,
            enabled = enabled,
            label = { Text(stringResource(Res.string.artifact_version_label)) },
            singleLine = true,
            isError = needsVersionChange,
            supportingText = if (needsVersionChange) {
                { Text(stringResource(Res.string.version_change_needed_message)) }
            } else {
                null
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            value = externalLink,
            onValueChange = onExternalLinkChange,
            enabled = enabled,
            label = { Text(stringResource(Res.string.artifact_external_link_label)) },
            singleLine = true
        )

        if (showType && availableTypes != null) {
            DropdownFormField(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                text = type?.name ?: stringResource(Res.string.select_label),
                label = stringResource(Res.string.artifact_type_label),
                options = availableTypes,
                onOptionSelected = { onTypeChange?.invoke(it) },
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

@Composable
fun ArtifactFormFields(
    name: String,
    onNameChange: (String) -> Unit,
    currentVersion: String,
    onVersionChange: (String) -> Unit,
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
    ArtifactFormFields(
        name = name,
        onNameChange = onNameChange,
        currentVersion = currentVersion,
        onVersionChange = onVersionChange,
        needsVersionChange = false,
        showType = true,
        type = type,
        onTypeChange = onTypeChange,
        inspectors = inspectors,
        onAddInspectorClick = onAddInspectorClick,
        onRemoveInspectorClick = onRemoveInspectorClick,
        availableTypes = availableTypes,
        externalLink = externalLink,
        onExternalLinkChange = onExternalLinkChange,
        modifier = modifier,
        enabled = enabled
    )
}

@Composable
fun ArtifactFormFields(
    name: String,
    onNameChange: (String) -> Unit,
    currentVersion: String,
    onVersionChange: (String) -> Unit,
    needsVersionChange: Boolean,
    inspectors: List<User>,
    onAddInspectorClick: () -> Unit,
    onRemoveInspectorClick: (User) -> Unit,
    availableTypes: List<ArtifactType>?,
    externalLink: String,
    onExternalLinkChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    ArtifactFormFields(
        name = name,
        onNameChange = onNameChange,
        currentVersion = currentVersion,
        onVersionChange = onVersionChange,
        needsVersionChange = needsVersionChange,
        showType = false,
        type = null,
        onTypeChange = null,
        inspectors = inspectors,
        onAddInspectorClick = onAddInspectorClick,
        onRemoveInspectorClick = onRemoveInspectorClick,
        availableTypes = availableTypes,
        externalLink = externalLink,
        onExternalLinkChange = onExternalLinkChange,
        modifier = modifier,
        enabled = enabled
    )
}

