package com.julia.imp.team.switcher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.text.getInitials
import com.julia.imp.common.ui.theme.ImpTheme
import com.julia.imp.common.ui.tooling.Preview
import com.julia.imp.team.Team
import com.julia.imp.team.switcher.TeamSwitcherDefaults.TeamIconButtonSize
import com.julia.imp.team.switcher.TeamSwitcherDefaults.TeamSwitcherPadding
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.switch_team_title
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamSwitcher(
    currentTeam: Team,
    onTeamChanged: (Team) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TeamSwitcherViewModel = viewModel { TeamSwitcherViewModel() },
) {
    val uiState by viewModel.uiState.collectAsState()

    TeamIcon(
        modifier = modifier,
        initials = currentTeam.name.getInitials(),
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
                Column(Modifier.fillMaxWidth().padding(TeamSwitcherPadding)) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(Res.string.switch_team_title),
                        style = MaterialTheme.typography.headlineSmall
                    )

                    if (uiState.isLoading) {
                        Box(Modifier.weight(1f, fill = false).fillMaxWidth()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center).padding(48.dp)
                            )
                        }
                    } else {
                        LazyColumn(Modifier.weight(1f, fill = false).fillMaxWidth()) {
                            uiState.teams?.let {
                                items(it) { team ->
                                    ListItem(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .clickable {
                                                onTeamChanged(team)
                                                viewModel.closeSwitcher()
                                            },
                                        headlineContent = { Text(team.name) },
                                        leadingContent = { TeamIcon(team.name.getInitials()) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeamIcon(
    initials: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(TeamIconButtonSize),
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
fun TeamIcon(
    initials: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(TeamIconButtonSize),
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

@Preview
@Composable
fun TeamSwitcherPreview() {
    ImpTheme {
        TeamSwitcher(
            currentTeam = Team(id = "", name = "Time da Julia"),
            onTeamChanged = {}
        )
    }
}

object TeamSwitcherDefaults {
    val TeamSwitcherPadding = 24.dp
    val TeamIconButtonSize = 40.dp
}