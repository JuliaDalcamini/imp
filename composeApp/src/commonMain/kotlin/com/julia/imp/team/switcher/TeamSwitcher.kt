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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.session.UserSession
import com.julia.imp.common.text.getInitials
import com.julia.imp.team.Team
import com.julia.imp.team.switcher.TeamSwitcherError.ErrorLoadingTeams
import com.julia.imp.team.switcher.TeamSwitcherError.ErrorSwitchingActiveTeam
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.create_team_label
import imp.composeapp.generated.resources.load_teams_error
import imp.composeapp.generated.resources.switch_team_error
import imp.composeapp.generated.resources.switch_team_title
import org.jetbrains.compose.resources.stringResource

private val TeamIconSize = 40.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamSwitcher(
    modifier: Modifier = Modifier,
    onTeamSwitch: (UserSession) -> Unit,
    viewModel: TeamSwitcherViewModel = viewModel { TeamSwitcherViewModel() },
) {
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.newSession) {
        uiState.newSession?.let { onTeamSwitch(it) }
    }
    
    TeamIcon(
        modifier = modifier,
        initials = uiState.currentTeam.name.getInitials(),
        onClick = { viewModel.openSwitcher() }
    )

    if (uiState.isSwitcherOpen) {
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
                        
                        uiState.isLoading -> {
                            TeamSwitcherListPlaceholder(Modifier.fillMaxWidth())
                        }

                        else -> {
                            TeamSwitcherList(
                                modifier = Modifier.weight(1f, fill = false).fillMaxWidth(),
                                teams = uiState.teams.orEmpty(),
                                onTeamClick = { viewModel.switchToTeam(it) },
                                onCreateTeamClick = {}
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
    onTeamClick: (Team) -> Unit,
    onCreateTeamClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items(teams) { team ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .animateItem()
                    .clickable { onTeamClick(team) },
                headlineContent = { Text(team.name) },
                leadingContent = { TeamIcon(team.name.getInitials()) }
            )
        }

        item("create") {
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .animateItem()
                    .clickable { onCreateTeamClick() },
                headlineContent = { Text(stringResource(Res.string.create_team_label)) },
                leadingContent = { CreateTeamIcon() }
            )
        }
    }
}

@Composable
private fun TeamIcon(
    initials: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(TeamIconSize),
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials.uppercase(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun TeamIcon(
    initials: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(TeamIconSize),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials.uppercase(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun CreateTeamIcon(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(TeamIconSize),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Add, null)
        }
    }
}