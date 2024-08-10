package com.julia.imp.team.inspector

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julia.imp.common.ui.dialog.BaseDialog
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.no_inspectors_available
import imp.composeapp.generated.resources.select_inspector_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun InspectorPickerDialog(
    availableInspectors: List<Inspector>?,
    onInspectorSelected: (Inspector) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseDialog(
        modifier = modifier,
        title = stringResource(Res.string.select_inspector_label),
        onDismissRequest = onDismissRequest
    ) {
        when {
            availableInspectors == null -> Placeholder(Modifier.fillMaxWidth())

            availableInspectors.isEmpty() -> Text(
                modifier = Modifier.padding(24.dp),
                text = stringResource(Res.string.no_inspectors_available)
            )

            else -> InspectorList(
                modifier = Modifier.fillMaxWidth(),
                inspectors = availableInspectors,
                onInspectorClick = {
                    onInspectorSelected(it)
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
private fun Placeholder(modifier: Modifier = Modifier) {
    Box(modifier) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center).padding(48.dp)
        )
    }
}

