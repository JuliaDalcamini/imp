package com.julia.imp.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julia.imp.common.ui.dialog.BaseDialog
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.no_users_available
import org.jetbrains.compose.resources.stringResource

@Composable
fun UserPickerDialog(
    title: String,
    availableUsers: List<User>?,
    onUserSelected: (User) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseDialog(
        modifier = modifier,
        title = title,
        onDismissRequest = onDismissRequest
    ) {
        when {
            availableUsers == null -> Placeholder(Modifier.fillMaxWidth())

            availableUsers.isEmpty() -> Text(
                modifier = Modifier.padding(24.dp),
                text = stringResource(Res.string.no_users_available)
            )

            else -> UserList(
                modifier = Modifier.fillMaxWidth(),
                users = availableUsers,
                onUserClick = {
                    onUserSelected(it)
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
private fun Placeholder(modifier: Modifier = Modifier) {
    Box(modifier) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center).padding(48.dp)
        )
    }
}

