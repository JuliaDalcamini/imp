package com.julia.imp.common.ui.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.julia.imp.common.ui.button.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    subtitle: String? = null,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            onBackClick?.let {
                BackButton(onClick = it)
            }
        },
        title = {
            if (subtitle.isNullOrBlank()) {
                Title(title = title)
            } else {
                CompoundTitle(title = title, subtitle = subtitle)
            }
        },
        actions = actions
    )
}