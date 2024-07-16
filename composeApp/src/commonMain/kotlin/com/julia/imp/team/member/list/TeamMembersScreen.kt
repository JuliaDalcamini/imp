package com.julia.imp.team.member.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import com.julia.imp.common.ui.dialog.ConfirmationDialog
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.dialog.TextInputDialog
import com.julia.imp.team.member.Role
import com.julia.imp.team.member.TeamMember
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.action_error_message
import imp.composeapp.generated.resources.action_error_title
import imp.composeapp.generated.resources.add_24px
import imp.composeapp.generated.resources.add_member_label
import imp.composeapp.generated.resources.add_member_title
import imp.composeapp.generated.resources.arrow_back_24px
import imp.composeapp.generated.resources.change_role_label
import imp.composeapp.generated.resources.change_role_title
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

@OptIn(ExperimentalMaterial3Api::class)
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
            TopAppBar(
                title = { Text(stringResource(Res.string.team_members_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(vectorResource(Res.drawable.arrow_back_24px), null)
                    }
                }
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
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = member.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = stringResource(Res.string.team_member_role_format, member.role.name),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (showOptions) {
                Box {
                    var expandOptions by remember { mutableStateOf(false) }

                    IconButton(onClick = { expandOptions = true }) {
                        Icon(vectorResource(Res.drawable.more_vert_24px), null)
                    }

                    TeamMemberOptionsDropdown(
                        expanded = expandOptions,
                        onDismissRequest = { expandOptions = false },
                        onChangeRoleClick = onChangeRoleClick,
                        onRemoveClick = onRemoveClick
                    )
                }
            }
        }
    }
}

@Composable
fun TeamMemberOptionsDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onChangeRoleClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(Res.string.change_role_label)) },
            leadingIcon = { Icon(vectorResource(Res.drawable.rule_settings_24px), null) },
            onClick = {
                onChangeRoleClick()
                onDismissRequest()
            }
        )

        DropdownMenuItem(
            text = { Text(stringResource(Res.string.remove_label)) },
            leadingIcon = { Icon(vectorResource(Res.drawable.person_remove_24px), null) },
            onClick = {
                onRemoveClick()
                onDismissRequest()
            }
        )
    }
}

@Composable
fun ChangeMemberRoleDialog(
    currentRole: Role,
    onDismissRequest: () -> Unit,
    onConfirm: (Role) -> Unit
) {
    // TODO: Implement role change UI

    TextInputDialog(
        title = stringResource(Res.string.change_role_title),
        initialValue = currentRole.name,
        onDismissRequest = onDismissRequest,
        onConfirm = { onConfirm(Role.valueOf(it)) }
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
    // TODO: Implement role selection UI

    TextInputDialog(
        title = stringResource(Res.string.add_member_title),
        placeholder = stringResource(Res.string.email_label),
        onDismissRequest = onDismissRequest,
        onConfirm = { onConfirm(it, Role.Viewer) }
    )
}