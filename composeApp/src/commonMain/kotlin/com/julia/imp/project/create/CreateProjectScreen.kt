package com.julia.imp.project.create

import androidx.compose.foundation.layout.Arrangement
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
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.form.DropdownFormField
import com.julia.imp.common.ui.form.FormField
import com.julia.imp.common.ui.form.SliderFormField
import com.julia.imp.common.ui.title.Title
import com.julia.imp.priority.MoscowPrioritizer
import com.julia.imp.priority.WiegersPrioritizer
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.arrow_back_24px
import imp.composeapp.generated.resources.complexity_weight_label
import imp.composeapp.generated.resources.create_project_error_message
import imp.composeapp.generated.resources.create_project_error_title
import imp.composeapp.generated.resources.create_project_label
import imp.composeapp.generated.resources.impact_weight_label
import imp.composeapp.generated.resources.inspectors_number_label
import imp.composeapp.generated.resources.moscow_label
import imp.composeapp.generated.resources.new_project_title
import imp.composeapp.generated.resources.prioritization_method_label
import imp.composeapp.generated.resources.project_name_label
import imp.composeapp.generated.resources.user_value_weight_label
import imp.composeapp.generated.resources.wiegers_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

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
                        Icon(vectorResource(Res.drawable.arrow_back_24px), null)
                    }
                },
                title = { Title(stringResource(Res.string.new_project_title)) }
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

                val inspectorOptions = listOf(2, 3, 4, 5)

                DropdownFormField(
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                    text = viewModel.uiState.totalInspectors.toString(),
                    label = stringResource(Res.string.inspectors_number_label),
                    options = inspectorOptions,
                    onOptionSelected = { viewModel.setInspectorCount(it) },
                    enabled = !viewModel.uiState.loading,
                    optionLabel = { optionNumber -> Text(optionNumber.toString()) }
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
                    WiegersPrioritizerFormFields(
                        modifier = Modifier.padding(top = 24.dp),
                        prioritizer = prioritizer,
                        onValueChange = { viewModel.setPrioritizer(it) },
                        enabled = !viewModel.uiState.loading
                    )
                }

                Spacer(Modifier.height(24.dp))
            }

            val isSumValid = viewModel.isWiegersSumValid()

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.create_project_label),
                onClick = { viewModel.createProject() },
                enabled = isSumValid && !viewModel.uiState.loading,
                loading = viewModel.uiState.loading
            )

            if (viewModel.uiState.error) {
                ErrorDialog(
                    title = stringResource(Res.string.create_project_error_title),
                    message = stringResource(Res.string.create_project_error_message),
                    onDismissRequest = { viewModel.dismissError() }
                )
            }
        }
    }
}

@Composable
fun WiegersPrioritizerFormFields(
    prioritizer: WiegersPrioritizer,
    onValueChange: (WiegersPrioritizer) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        val totalWeight =
            prioritizer.userValueWeight + prioritizer.complexityWeight + prioritizer.impactWeight

        WeightSliderFormField(
            label = stringResource(Res.string.user_value_weight_label),
            enabled = enabled,
            value = prioritizer.userValueWeight,
            onValueChange = {
                val newTotalWeight = totalWeight - prioritizer.userValueWeight + it

                if (newTotalWeight <= 1 && it >= 0.1f) {
                    onValueChange(prioritizer.copy(userValueWeight = it))
                }
            }
        )

        WeightSliderFormField(
            label = stringResource(Res.string.complexity_weight_label),
            enabled = enabled,
            value = prioritizer.complexityWeight,
            onValueChange = {
                val newTotalWeight = totalWeight - prioritizer.complexityWeight + it

                if (newTotalWeight <= 1 && it >= 0.1f) {
                    onValueChange(prioritizer.copy(complexityWeight = it))
                }
            }
        )

        WeightSliderFormField(
            label = stringResource(Res.string.impact_weight_label),
            enabled = enabled,
            value = prioritizer.impactWeight,
            onValueChange = {
                val newTotalWeight = totalWeight - prioritizer.impactWeight + it

                if (newTotalWeight <= 1 && it >= 0.1f) {
                    onValueChange(prioritizer.copy(impactWeight = it))
                }
            }
        )
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
        steps = 9,
        valueRange = 0f..1f,
        valueText = String.format("%.1f", value)
    )
}