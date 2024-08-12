package com.julia.imp.user

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
fun UserList(
    users: List<User>,
    onUserClick: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items(users) { user ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onUserClick(user) },
                headlineContent = { Text(user.fullName) },
                leadingContent = { Avatar(user.fullName.getInitials()) }
            )
        }
    }
}