package com.julia.imp.team.switcher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.session.UserSession
import com.julia.imp.common.text.getInitials
import com.julia.imp.common.ui.avatar.Avatar
import com.julia.imp.common.ui.avatar.AvatarSize
import com.julia.imp.team.Team
import com.julia.imp.team.switcher.TeamSwitcherError.ErrorLoadingTeams
import com.julia.imp.team.switcher.TeamSwitcherError.ErrorSwitchingActiveTeam
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.add_24px
import imp.composeapp.generated.resources.create_team_label
import imp.composeapp.generated.resources.current_team_label
import imp.composeapp.generated.resources.load_teams_error
import imp.composeapp.generated.resources.logout_24px
import imp.composeapp.generated.resources.logout_label
import imp.composeapp.generated.resources.settings_24px
import imp.composeapp.generated.resources.switch_team_error
import imp.composeapp.generated.resources.switch_team_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamSwitcher(
    modifier: Modifier = Modifier,
    onTeamSwitch: (UserSession) -> Unit,
    onManageTeamClick: () -> Unit,
    onCreateTeamClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: TeamSwitcherViewModel = viewModel { TeamSwitcherViewModel() }
) {
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.newSession) {
        uiState.newSession?.let { onTeamSwitch(it) }
    }
    
    Avatar(
        modifier = modifier,
        initials = uiState.currentTeam.name.getInitials(),
        onClick = { viewModel.openSwitcher() }
    )

    if (uiState.switcherOpen) {
        BasicAlertDialog(
            onDismissRequest = { viewModel.closeSwitcher() }
        ) {
            Surface(
                shape = RoundedCornerShape(28.0.dp),
                tonalElevation = 6.0.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .wrapContentHeight()
                ) {
                    Text(
                        text = stringResource(Res.string.switch_team_title),
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(Modifier.height(12.dp))

                    when {
                        uiState.error != null -> {
                            TeamSwitcherErrorMessage(
                                modifier = Modifier.fillMaxWidth(),
                                error = uiState.error
                            )
                        }
                        
                        uiState.loading -> {
                            TeamSwitcherListPlaceholder(Modifier.fillMaxWidth())
                        }

                        else -> {
                            TeamSwitcherList(
                                modifier = Modifier.weight(1f, fill = false).fillMaxWidth(),
                                teams = uiState.teams.orEmpty(),
                                currentTeam = uiState.currentTeam,
                                showManageTeamOption = uiState.showManageOption,
                                onTeamClick = { viewModel.switchToTeam(it) },
                                onManageTeamClick = {
                                    onManageTeamClick()
                                    viewModel.closeSwitcher()
                                },
                                onCreateTeamClick = {
                                    onCreateTeamClick()
                                    viewModel.closeSwitcher()
                                },
                                onLogoutClick = {
                                    onLogoutClick()
                                    viewModel.closeSwitcher()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamSwitcherListPlaceholder(modifier: Modifier = Modifier) {
    Box(modifier) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center).padding(48.dp)
        )
    }
}

@Composable
private fun TeamSwitcherErrorMessage(
    modifier: Modifier = Modifier,
    error: TeamSwitcherError
) {
    Box(modifier) {
        Text(
            text = when (error) {
                ErrorLoadingTeams -> stringResource(Res.string.load_teams_error)
                ErrorSwitchingActiveTeam -> stringResource(Res.string.switch_team_error)
            }
        )
    }
}

@Composable
private fun TeamSwitcherList(
    teams: List<Team>,
    currentTeam: Team,
    showManageTeamOption: Boolean,
    onTeamClick: (Team) -> Unit,
    onManageTeamClick: () -> Unit,
    onCreateTeamClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val otherTeams = teams.filterNot { it == currentTeam }

    LazyColumn(modifier) {
        item("currentTeam") {
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .animateItem(),
                headlineContent = { Text(currentTeam.name) },
                leadingContent = { Avatar(currentTeam.name.getInitials()) },
                supportingContent = { Text(stringResource(Res.string.current_team_label)) },
                trailingContent = {
                    if (showManageTeamOption) {
                        IconButton(onClick = onManageTeamClick) {
                            Icon(vectorResource(Res.drawable.settings_24px), null)
                        }
                    }
                }
            )
        }

        item("divider") {
            HorizontalDivider(Modifier.fillMaxWidth().padding(bottom = 8.dp))
        }

        item("create") {
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .animateItem()
                    .clickable { onCreateTeamClick() },
                headlineContent = { Text(stringResource(Res.string.create_team_label)) },
                leadingContent = { ListItemIcon(vectorResource(Res.drawable.add_24px)) }
            )
        }

        item("logout") {
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .animateItem()
                    .clickable { onLogoutClick() },
                headlineContent = { Text(stringResource(Res.string.logout_label)) },
                leadingContent = { ListItemIcon(vectorResource(Res.drawable.logout_24px)) }
            )
        }

        items(otherTeams) { team ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .animateItem()
                    .clickable { onTeamClick(team) },
                headlineContent = { Text(team.name) },
                leadingContent = { Avatar(team.name.getInitials()) }
            )
        }
    }
}

@Composable
private fun ListItemIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(AvatarSize.Medium.shapeSize),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null)
        }
    }
}