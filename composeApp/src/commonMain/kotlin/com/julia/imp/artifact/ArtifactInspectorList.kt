package com.julia.imp.artifact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julia.imp.common.text.getInitials
import com.julia.imp.common.ui.avatar.Avatar
import com.julia.imp.common.ui.avatar.AvatarSize
import com.julia.imp.user.User
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.add_20px
import imp.composeapp.generated.resources.add_label
import imp.composeapp.generated.resources.close_20px
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ArtifactInspectorList(
    inspectors: List<User>,
    onAddClick: () -> Unit,
    onRemoveClick: (User) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.Top
    ) {
        for (inspector in inspectors) {
            InputChip(
                label = { Text(inspector.fullName) },
                onClick = { if (!readOnly) onRemoveClick(inspector) },
                selected = true,
                enabled = enabled,
                shape = RoundedCornerShape(16.dp),
                avatar = {
                    Avatar(
                        initials = inspector.fullName.getInitials(),
                        size = AvatarSize.Small,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (!readOnly) {
                        Icon(vectorResource(Res.drawable.close_20px), null)
                    }
                }
            )
        }

        if (!readOnly) {
            AssistChip(
                label = { Text(stringResource(Res.string.add_label)) },
                onClick = onAddClick,
                enabled = enabled,
                leadingIcon = { Icon(vectorResource(Res.drawable.add_20px), null) }
            )
        }
    }
}