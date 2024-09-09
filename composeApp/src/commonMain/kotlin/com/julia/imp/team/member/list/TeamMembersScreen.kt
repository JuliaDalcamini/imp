package com.julia.imp.team.member.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.text.formatAsCurrency
import com.julia.imp.common.text.getInitials
import com.julia.imp.common.ui.avatar.Avatar
import com.julia.imp.common.ui.dialog.ConfirmationDialog
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.dialog.SelectionDialog
import com.julia.imp.common.ui.dialog.TextInputDialog
import com.julia.imp.common.ui.topbar.TopBar
import com.julia.imp.team.member.Role
import com.julia.imp.team.member.TeamMember
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.action_error_message
import imp.composeapp.generated.resources.action_error_title
import imp.composeapp.generated.resources.add_24px
import imp.composeapp.generated.resources.add_member_label
import imp.composeapp.generated.resources.add_member_title
import imp.composeapp.generated.resources.change_cost_label
import imp.composeapp.generated.resources.change_role_label
import imp.composeapp.generated.resources.change_role_title
import imp.composeapp.generated.resources.currency_exchange_24px
import imp.composeapp.generated.resources.current_hourly_cost_format
import imp.composeapp.generated.resources.email_label
import imp.composeapp.generated.resources.more_vert_24px
import imp.composeapp.generated.resources.no_projects_message
import imp.composeapp.generated.resources.person_remove_24px
import imp.composeapp.generated.resources.projects_error_message
import imp.composeapp.generated.resources.refresh_24px
import imp.composeapp.generated.resources.remove_label
import imp.composeapp.generated.resources.remove_member_message
import imp.composeapp.generated.resources.remove_member_title
import imp.composeapp.generated.resources.rule_settings_24px
import imp.composeapp.generated.resources.team_member_role_format
import imp.composeapp.generated.resources.team_members_title
import imp.composeapp.generated.resources.try_again_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun TeamMembersScreen(
    onBackClick: () -> Unit,
    viewModel: TeamMembersViewModel = viewModel { TeamMembersViewModel() }
) {
    LaunchedEffect(Unit) {
        viewModel.getMembers()
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.team_members_title),
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            if (viewModel.uiState.showAddButton) {
                AddMemberButton(onClick = { viewModel.askToAdd() })
            }
        }
    ) { paddingValues ->
        Box(Modifier.fillMaxSize()) {
            if (viewModel.uiState.loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(paddingValues)
                )
            }

            viewModel.uiState.members?.let { members ->
                if (members.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .consumeWindowInsets(paddingValues),
                        contentPadding = paddingValues
                    ) {
                        items(members) { member ->
                            TeamMemberListItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 8.dp)
                                    .animateItem(),
                                member = member,
                                showOptions = viewModel.uiState.showOptions,
                                onChangeRoleClick = { viewModel.askToChangeRole(member) },
                                onChangeCostClick = { viewModel.askToChangeCost(member) },
                                onRemoveClick = { viewModel.askToRemove(member) }
                            )
                        }

                        item {
                            Spacer(Modifier.height(56.dp))
                        }
                    }
                } else {
                    Text(
                        modifier = Modifier.padding(24.dp).align(Alignment.Center),
                        text = stringResource(Res.string.no_projects_message)
                    )
                }
            }

            if (viewModel.uiState.error) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .consumeWindowInsets(paddingValues)
                        .padding(paddingValues)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(Res.string.projects_error_message),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )

                    TextButton(
                        modifier = Modifier.padding(top = 4.dp),
                        onClick = { viewModel.getMembers() }
                    ) {
                        Icon(vectorResource(Res.drawable.refresh_24px), null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(Res.string.try_again_label))
                    }
                }
            }
        }
    }

    viewModel.uiState.memberToRemove?.let { member ->
        RemoveMemberDialog(
            memberName = member.fullName,
            onDismissRequest = { viewModel.dismissRemoval() },
            onConfirm = { viewModel.remove(member) }
        )
    }

    viewModel.uiState.memberToChangeRole?.let { member ->
        ChangeMemberRoleDialog(
            currentRole = member.role,
            onDismissRequest = { viewModel.dismissRoleChange() },
            onConfirm = { newRole -> viewModel.changeRole(member, newRole) }
        )
    }

    if (viewModel.uiState.showAddDialog) {
        AddMemberDialog(
            onDismissRequest = { viewModel.dismissAdding() },
            onConfirm = { email, role -> viewModel.add(email, role) }
        )
    }

    viewModel.uiState.memberToChangeCost?.let { member ->
        ChangeCostDialog(
            currentCost = member.hourlyCost,
            onDismissRequest = { viewModel.dismissCostChange() },
            onConfirm = { newCost -> viewModel.changeCost(member, newCost.toDouble()) }
        )
    }

    if (viewModel.uiState.actionError) {
        ErrorDialog(
            title = stringResource(Res.string.action_error_title),
            message = stringResource(Res.string.action_error_message),
            onDismissRequest = { viewModel.dismissActionError() }
        )
    }
}

@Composable
fun AddMemberButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        text = { Text(stringResource(Res.string.add_member_label)) },
        icon = { Icon(vectorResource(Res.drawable.add_24px), null) },
        onClick = onClick
    )
}

@Composable
fun TeamMemberListItem(
    member: TeamMember,
    showOptions: Boolean,
    onChangeRoleClick: () -> Unit,
    onChangeCostClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier) {
        val compact = maxWidth < 480.dp

        ListItem(
            modifier = Modifier.fillMaxWidth(),
            leadingContent = { Avatar(member.fullName.getInitials()) },
            headlineContent = {
                Text(
                    text = member.fullName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(
                            Res.string.team_member_role_format,
                            member.role.getLabel()
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (member.role != Role.Viewer) {
                        Text(
                            text = stringResource(
                                Res.string.current_hourly_cost_format,
                                member.hourlyCost.formatAsCurrency()
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            },
            trailingContent = {
                if (showOptions) {
                    TeamMemberOptions(
                        compact = compact,
                        showChangeCostOption = member.role != Role.Viewer,
                        onChangeRoleClick = onChangeRoleClick,
                        onChangeCostClick = onChangeCostClick,
                        onRemoveClick = onRemoveClick
                    )
                }
            }
        )
    }
}

@Composable
fun TeamMemberOptions(
    compact: Boolean,
    showChangeCostOption: Boolean,
    onChangeRoleClick: () -> Unit,
    onChangeCostClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        var expanded by remember { mutableStateOf(false) }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (!compact) {
                IconButton(onClick = onChangeRoleClick) {
                    Icon(vectorResource(Res.drawable.rule_settings_24px), null)
                }
            }

            IconButton(onClick = { expanded = true }) {
                Icon(vectorResource(Res.drawable.more_vert_24px), null)
            }
        }

        DropdownMenu(
            modifier = modifier,
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (compact) {
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.change_role_label)) },
                    leadingIcon = { Icon(vectorResource(Res.drawable.rule_settings_24px), null) },
                    onClick = {
                        onChangeRoleClick()
                        expanded = false
                    }
                )
            }

            if (showChangeCostOption) {
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.change_cost_label)) },
                    leadingIcon = { Icon(vectorResource(Res.drawable.currency_exchange_24px), null) },
                    onClick = {
                        onChangeCostClick()
                        expanded = false
                    }
                )
            }

            DropdownMenuItem(
                text = { Text(stringResource(Res.string.remove_label)) },
                leadingIcon = { Icon(vectorResource(Res.drawable.person_remove_24px), null) },
                onClick = {
                    onRemoveClick()
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun ChangeMemberRoleDialog(
    currentRole: Role,
    onDismissRequest: () -> Unit,
    onConfirm: (Role) -> Unit
) {
    SelectionDialog(
        title = stringResource(Res.string.change_role_title),
        options = Role.entries,
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        initialSelection = currentRole,
        optionLabel = { role -> Text(role.getLabel()) }
    )
}

@Composable
fun RemoveMemberDialog(
    memberName: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    ConfirmationDialog(
        title = stringResource(Res.string.remove_member_title),
        message = stringResource(Res.string.remove_member_message, memberName),
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}

@Composable
fun AddMemberDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (String, Role) -> Unit
) {
    TextInputDialog(
        title = stringResource(Res.string.add_member_title),
        placeholder = stringResource(Res.string.email_label),
        onDismissRequest = onDismissRequest,
        onConfirm = { onConfirm(it, Role.Viewer) }
    )
}

@Composable
fun ChangeCostDialog(
    currentCost: Double,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    TextInputDialog(
        title = stringResource(Res.string.change_cost_label),
        initialValue = currentCost.toString(),
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}