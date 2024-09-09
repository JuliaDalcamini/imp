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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.session.UserSession
import com.julia.imp.common.ui.button.PrimaryButton
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.topbar.TopBar
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.create_team_error_message
import imp.composeapp.generated.resources.create_team_error_title
import imp.composeapp.generated.resources.create_team_label
import imp.composeapp.generated.resources.new_team_title
import imp.composeapp.generated.resources.team_name_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreateTeamScreen(
    onBackClick: () -> Unit,
    onTeamCreated: (UserSession) -> Unit,
    viewModel: CreateTeamViewModel = viewModel { CreateTeamViewModel() }
) {
    LaunchedEffect(viewModel.uiState.newSession) {
        viewModel.uiState.newSession?.let { onTeamCreated(it) }
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopBar(
                title = stringResource(Res.string.new_team_title),
                onBackClick = onBackClick
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
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
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
                onClick = { viewModel.createTeam() },
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