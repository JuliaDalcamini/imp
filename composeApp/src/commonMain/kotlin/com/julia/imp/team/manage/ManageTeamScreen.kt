package com.julia.imp.team.manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.session.requireTeam
import com.julia.imp.common.text.formatAsCurrency
import com.julia.imp.common.ui.dialog.TextInputDialog
import com.julia.imp.common.ui.topbar.TopBar
import com.julia.imp.team.Team
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.change_default_cost_label
import imp.composeapp.generated.resources.current_hourly_cost_format
import imp.composeapp.generated.resources.current_name_format
import imp.composeapp.generated.resources.manage_team_members_description
import imp.composeapp.generated.resources.manage_team_members_label
import imp.composeapp.generated.resources.manage_team_title
import imp.composeapp.generated.resources.rename_label
import imp.composeapp.generated.resources.rename_team_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ManageTeamScreen(
    onBackClick: () -> Unit,
    onManageMembersClick: () -> Unit,
    onTeamUpdate: (Team) -> Unit,
    viewModel: ManageTeamViewModel = viewModel { ManageTeamViewModel() }
) {
    LaunchedEffect(viewModel.uiState.updatedTeam) {
        viewModel.uiState.updatedTeam?.let {
            onTeamUpdate(it)
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.manage_team_title),
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues)
        ) {
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.showRenameDialog() },
                headlineContent = { Text(stringResource(Res.string.rename_label)) },
                supportingContent = { Text(stringResource(Res.string.current_name_format, requireTeam().name)) }
            )

            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.showUpdateDefaultCostDialog() },
                headlineContent = { Text(stringResource(Res.string.change_default_cost_label)) },
                supportingContent = {
                    Text(
                        stringResource(
                            Res.string.current_hourly_cost_format,
                            requireTeam().defaultHourlyCost.formatAsCurrency()
                        )
                    )
                }
            )

            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onManageMembersClick() },
                headlineContent = { Text(stringResource(Res.string.manage_team_members_label)) },
                supportingContent = { Text(stringResource(Res.string.manage_team_members_description)) }
            )
        }
    }

    if (viewModel.uiState.showRenameDialog) {
        RenameTeamDialog(
            teamName = requireTeam().name,
            onDismissRequest = { viewModel.dismissRenameDialog() },
            onConfirm = { newName -> viewModel.renameTeam(requireTeam(), newName) }
        )
    }

    if (viewModel.uiState.showUpdateDefaultCostDialog) {
        ChangeDefaultCostDialog(
            currentCost = requireTeam().defaultHourlyCost,
            onDismissRequest = { viewModel.dismissDefaultCostDialog() },
            onConfirm = { newDefaultHourlyCost -> viewModel.updateDefaultHourlyCost(requireTeam(), newDefaultHourlyCost.toDouble()) }
        )
    }
}

@Composable
fun RenameTeamDialog(
    teamName: String,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    TextInputDialog(
        title = stringResource(Res.string.rename_team_title),
        initialValue = teamName,
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}

@Composable
fun ChangeDefaultCostDialog(
    currentCost: Double,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    TextInputDialog(
        title = stringResource(Res.string.change_default_cost_label),
        initialValue = currentCost.toString(),
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}