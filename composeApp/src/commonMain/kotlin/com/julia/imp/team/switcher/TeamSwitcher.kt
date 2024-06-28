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
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.text.getInitials
import com.julia.imp.team.Team
import com.julia.imp.team.switcher.TeamSwitcherError.ErrorLoadingTeams
import com.julia.imp.team.switcher.TeamSwitcherError.ErrorSwitchingActiveTeam
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.load_teams_error
import imp.composeapp.generated.resources.switch_team_error
import imp.composeapp.generated.resources.switch_team_title
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamSwitcher(
    modifier: Modifier = Modifier,
    viewModel: TeamSwitcherViewModel = viewModel { TeamSwitcherViewModel() },
) {
    TeamIcon(
        modifier = modifier,
        initials = viewModel.currentTeam.name.getInitials(),
        onClick = { viewModel.openSwitcher() }
    )

    if (viewModel.uiState.isSwitcherOpen) {
        BasicAlertDialog(
            onDismissRequest = { viewModel.closeSwitcher() }
        ) {
            Surface(
                shape = RoundedCornerShape(28.0.dp),
                tonalElevation = 6.0.dp
            ) {
                Column(Modifier.fillMaxWidth().padding(24.dp)) {
                    Text(
                        text = stringResource(Res.string.switch_team_title),
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(Modifier.height(12.dp))

                    if (viewModel.uiState.isLoading) {
                        TeamSwitcherListPlaceholder(Modifier.fillMaxWidth())
                    } else {
                        TeamSwitcherList(
                            modifier = Modifier.weight(1f, fill = false).fillMaxWidth(),
                            teams = viewModel.uiState.teams.orEmpty(),
                            onTeamClick = { viewModel.switchToTeam(it) }
                        )
                    }

                    viewModel.uiState.error?.let { error ->
                        TeamSwitcherErrorMessage(
                            modifier = Modifier.fillMaxWidth(),
                            error = error
                        )
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
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier) {
        items(teams) { team ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable { onTeamClick(team) },
                headlineContent = { Text(team.name) },
                leadingContent = { TeamIcon(team.name.getInitials()) }
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
        modifier = modifier.size(40.dp),
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box(Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
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
        modifier = modifier.size(40.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box(Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = initials.uppercase(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}