package com.julia.imp.common.ui.avatar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Avatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.Medium,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    border: BorderStroke? = null
) {
    Surface(
        modifier = modifier.size(size.shapeSize),
        shape = CircleShape,
        color = color,
        border = border
    ) {
        AvatarContents(
            modifier = Modifier.fillMaxSize(),
            initials = initials,
            fontSize = size.fontSize
        )
    }
}

@Composable
fun Avatar(
    initials: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.Medium,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    border: BorderStroke? = null
) {
    Surface(
        modifier = modifier.size(size.shapeSize),
        onClick = onClick,
        shape = CircleShape,
        color = color,
        border = border
    ) {
        AvatarContents(
            modifier = Modifier.fillMaxSize(),
            initials = initials,
            fontSize = size.fontSize
        )
    }
}

@Composable
private fun AvatarContents(
    initials: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials.uppercase(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontSize = fontSize
        )
    }
}

enum class AvatarSize(val shapeSize: Dp, val fontSize: TextUnit) {
    Small(shapeSize = 24.dp, fontSize = 10.sp),
    Medium(shapeSize = 40.dp, fontSize = 16.sp)
}