package com.julia.imp.team.create

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.ui.button.PrimaryButton
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.title.Title
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.arrow_back_24px
import imp.composeapp.generated.resources.create_team_error_message
import imp.composeapp.generated.resources.create_team_error_title
import imp.composeapp.generated.resources.create_team_label
import imp.composeapp.generated.resources.new_team_title
import imp.composeapp.generated.resources.team_name_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamScreen(
    onBackClick: () -> Unit,
    viewModel: CreateTeamViewModel = viewModel { CreateTeamViewModel() }
) {
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(vectorResource(Res.drawable.arrow_back_24px), null)
                    }
                },
                title = { Title(stringResource(Res.string.new_team_title)) }
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
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.uiState.name,
                    onValueChange = { viewModel.setName(it) },
                    enabled = !viewModel.uiState.loading,
                    label = { Text(stringResource(Res.string.team_name_label)) },
                    singleLine = true
                )

                Spacer(Modifier.height(24.dp))
            }

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.create_team_label),
                onClick = { viewModel.createProject() },
                enabled = !viewModel.uiState.loading,
                loading = viewModel.uiState.loading
            )
        }
    }

    if (viewModel.uiState.error) {
        ErrorDialog(
            title = stringResource(Res.string.create_team_error_title),
            message = stringResource(Res.string.create_team_error_message),
            onDismissRequest = { viewModel.dismissError() }
        )
    }
}