package com.julia.imp.team

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.julia.imp.common.ui.theme.ImpTheme

@Composable
@Preview
fun NoTeamsScreenPreview() {
    ImpTheme {
        NoTeamsScreen(onCreateTeamClick = {})
    }
}