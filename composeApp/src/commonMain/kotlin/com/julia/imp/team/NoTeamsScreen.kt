package com.julia.imp.team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.julia.imp.common.ui.button.PrimaryButton
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.create_team_label
import imp.composeapp.generated.resources.no_teams_message
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoTeamsScreen(
    onCreateTeamClick: () -> Unit
) {
    Scaffold { paddingValues ->
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
                text = stringResource(Res.string.no_teams_message),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            PrimaryButton(
                modifier = Modifier.padding(top = 16.dp),
                label = stringResource(Res.string.create_team_label),
                onClick = onCreateTeamClick
            )
        }
    }
}
