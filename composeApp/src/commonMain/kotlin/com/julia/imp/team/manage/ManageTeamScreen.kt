package com.julia.imp.team.manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.julia.imp.common.session.requireTeam
import com.julia.imp.common.ui.title.Title
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.arrow_back_24px
import imp.composeapp.generated.resources.current_name_format
import imp.composeapp.generated.resources.manage_team_members_description
import imp.composeapp.generated.resources.manage_team_members_label
import imp.composeapp.generated.resources.manage_team_title
import imp.composeapp.generated.resources.rename_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTeamScreen(
    onBackClick: () -> Unit,
    onManageMembersClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(vectorResource(Res.drawable.arrow_back_24px), null)
                    }
                },
                title = { Title(stringResource(Res.string.manage_team_title)) }
            )
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {},
                headlineContent = { Text(stringResource(Res.string.rename_label)) },
                supportingContent = { Text(stringResource(Res.string.current_name_format, requireTeam().name)) }
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
}