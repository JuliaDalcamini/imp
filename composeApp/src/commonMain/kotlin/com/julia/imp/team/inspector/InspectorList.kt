package com.julia.imp.team.inspector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.julia.imp.common.text.getInitials
import com.julia.imp.common.ui.avatar.Avatar

@Composable
fun InspectorList(
    inspectors: List<Inspector>,
    onInspectorClick: (Inspector) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items(inspectors) { inspector ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onInspectorClick(inspector) },
                headlineContent = { Text(inspector.fullName) },
                leadingContent = { Avatar(inspector.fullName.getInitials()) }
            )
        }
    }
}