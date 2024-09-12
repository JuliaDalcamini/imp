package com.julia.imp.inspection.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.artifact.Artifact
import com.julia.imp.common.ui.button.PrimaryButton
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.form.RadioGroupFormField
import com.julia.imp.common.ui.topbar.TopBar
import com.julia.imp.inspection.Inspection
import com.julia.imp.inspection.answer.AnswerOption
import com.julia.imp.question.Question
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.defect_detail_label
import imp.composeapp.generated.resources.finish_inspection_error_message
import imp.composeapp.generated.resources.finish_inspection_error_title
import imp.composeapp.generated.resources.finish_inspection_label
import imp.composeapp.generated.resources.load_error_message
import imp.composeapp.generated.resources.load_error_title
import imp.composeapp.generated.resources.new_inspection_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreateInspectionScreen(
    artifact: Artifact,
    onBackClick: () -> Unit,
    onInspectionCreated: (Inspection) -> Unit,
    viewModel: CreateInspectionViewModel = viewModel { CreateInspectionViewModel() }
) {
    LaunchedEffect(viewModel.uiState.createdInspection) {
        viewModel.uiState.createdInspection?.let { onInspectionCreated(it) }
    }

    LaunchedEffect(artifact) {
        viewModel.initialize(artifact)
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopBar(
                title = stringResource(Res.string.new_inspection_title),
                subtitle = artifact.name,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        if (viewModel.uiState.loading) {
            Placeholder(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(paddingValues)
                    .consumeWindowInsets(PaddingValues(24.dp))
                    .padding(paddingValues)
                    .padding(24.dp)
            )
        } else {
            CreateInspectionForm(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(paddingValues)
                    .consumeWindowInsets(PaddingValues(24.dp))
                    .padding(paddingValues)
                    .padding(24.dp),
                questions = viewModel.uiState.questions,
                onAnswerChange = { question, answer ->
                    viewModel.setAnswer(question, answer)
                },
                onDefectDetailChange = { question, detail ->
                    viewModel.setDefectDetail(question, detail) },
                canSubmit = viewModel.uiState.canCreate,
                submitting = viewModel.uiState.saving,
                onSubmit = { viewModel.createInspection() }
            )
        }
    }

    if (viewModel.uiState.createError) {
        ErrorDialog(
            title = stringResource(Res.string.finish_inspection_error_title),
            message = stringResource(Res.string.finish_inspection_error_message),
            onDismissRequest = { viewModel.dismissCreateError() }
        )
    }

    if (viewModel.uiState.loadError) {
        ErrorDialog(
            title = stringResource(Res.string.load_error_title),
            message = stringResource(Res.string.load_error_message),
            onDismissRequest = {
                viewModel.dismissLoadError()
                onBackClick()
            }
        )
    }
}

@Composable
private fun Placeholder(modifier: Modifier = Modifier) {
    Box(modifier) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
}

@Composable
fun CreateInspectionForm(
    questions: SnapshotStateMap<Question, QuestionState>?,
    onAnswerChange: (Question, AnswerOption) -> Unit,
    onDefectDetailChange: (Question, String) -> Unit,
    canSubmit: Boolean,
    submitting: Boolean,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            questions?.forEach { entry ->
                val question = entry.key
                val selectedAnswer = entry.value.answer

                RadioGroupFormField(
                    modifier = Modifier.fillMaxWidth(),
                    label = question.text,
                    options = AnswerOption.entries,
                    selectedOption = selectedAnswer,
                    onSelectionChange = { onAnswerChange(question, it) },
                    enabled = !submitting,
                    optionLabel = { Text(it.getLabel()) }
                )

                if (entry.value.answer == AnswerOption.No) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = entry.value.defectDetail.orEmpty(),
                        onValueChange = { text -> onDefectDetailChange(question, text) },
                        label = { Text(stringResource(Res.string.defect_detail_label)) },
                        singleLine = true
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.finish_inspection_label),
            onClick = onSubmit,
            enabled = canSubmit && !submitting,
            loading = submitting
        )
    }
}
