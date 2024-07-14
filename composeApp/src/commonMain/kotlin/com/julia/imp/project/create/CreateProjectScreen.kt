package com.julia.imp.project.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.ui.button.PrimaryButton
import com.julia.imp.common.ui.form.FormField
import com.julia.imp.common.ui.form.SliderFormField
import com.julia.imp.priority.MoscowPrioritizer
import com.julia.imp.priority.WiegersPrioritizer
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.complexity_weight_label
import imp.composeapp.generated.resources.create_project_label
import imp.composeapp.generated.resources.impact_weight_label
import imp.composeapp.generated.resources.moscow_label
import imp.composeapp.generated.resources.new_project_title
import imp.composeapp.generated.resources.prioritization_method_label
import imp.composeapp.generated.resources.project_name_label
import imp.composeapp.generated.resources.user_value_weight_label
import imp.composeapp.generated.resources.wiegers_label
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(
    onBackClick: () -> Unit,
    onProjectCreated: () -> Unit,
    viewModel: CreateProjectViewModel = viewModel { CreateProjectViewModel() }
) {
    LaunchedEffect(viewModel.uiState.created) {
        if (viewModel.uiState.created) onProjectCreated()
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, null)
                    }
                 },
                title = { Text(stringResource(Res.string.new_project_title)) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .consumeWindowInsets(PaddingValues(24.dp))
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            val scrollState = rememberScrollState()

            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                val prioritizer = viewModel.uiState.prioritizer

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.uiState.name,
                    onValueChange = { viewModel.setName(it) },
                    enabled = !viewModel.uiState.loading,
                    label = { Text(stringResource(Res.string.project_name_label)) },
                    singleLine = true
                )

                FormField(
                    modifier = Modifier.padding(top = 24.dp),
                    label = stringResource(Res.string.prioritization_method_label)
                ) {
                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = prioritizer == MoscowPrioritizer,
                            enabled = !viewModel.uiState.loading,
                            onClick = { viewModel.setPrioritizer(MoscowPrioritizer) },
                            shape = SegmentedButtonDefaults.itemShape(0, 2),
                            label = { Text(stringResource(Res.string.moscow_label)) }
                        )

                        SegmentedButton(
                            selected = prioritizer is WiegersPrioritizer,
                            enabled = !viewModel.uiState.loading,
                            onClick = { viewModel.setPrioritizer(WiegersPrioritizer()) },
                            shape = SegmentedButtonDefaults.itemShape(1, 2),
                            label = { Text(stringResource(Res.string.wiegers_label)) }
                        )
                    }
                }

                if (prioritizer is WiegersPrioritizer) {
                    WeightSliderFormField(
                        modifier = Modifier.padding(top = 24.dp),
                        label = stringResource(Res.string.user_value_weight_label),
                        enabled = !viewModel.uiState.loading,
                        value = prioritizer.userValueWeight.toFloat(),
                        onValueChange = {
                            viewModel.setPrioritizer(prioritizer.copy(userValueWeight = it.toDouble()))
                        }
                    )

                    WeightSliderFormField(
                        modifier = Modifier.padding(top = 24.dp),
                        label = stringResource(Res.string.complexity_weight_label),
                        enabled = !viewModel.uiState.loading,
                        value = prioritizer.complexityWeight.toFloat(),
                        onValueChange = {
                            viewModel.setPrioritizer(prioritizer.copy(complexityWeight = it.toDouble()))
                        }
                    )

                    WeightSliderFormField(
                        modifier = Modifier.padding(top = 24.dp),
                        label = stringResource(Res.string.impact_weight_label),
                        enabled = !viewModel.uiState.loading,
                        value = prioritizer.impactWeight.toFloat(),
                        onValueChange = {
                            viewModel.setPrioritizer(prioritizer.copy(impactWeight = it.toDouble()))
                        }
                    )
                }

                Spacer(Modifier.height(24.dp))
            }

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.create_project_label),
                onClick = { viewModel.createProject() },
                enabled = !viewModel.uiState.loading,
                loading = viewModel.uiState.loading
            )
        }
    }
}

@Composable
fun WeightSliderFormField(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    SliderFormField(
        modifier = modifier,
        label = label,
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        steps = 3,
        valueRange = 1f..5f,
        valueText = value.toInt().toString()
    )
}