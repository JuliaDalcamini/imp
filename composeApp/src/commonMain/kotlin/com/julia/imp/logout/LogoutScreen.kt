package com.julia.imp.logout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LogoutScreen(
    onLogout: () -> Unit,
    viewModel: LogoutViewModel = viewModel { LogoutViewModel() }
) {
    LaunchedEffect(Unit) {
        viewModel.logout()
    }

    LaunchedEffect(viewModel.uiState.loggedOut) {
        if (viewModel.uiState.loggedOut) onLogout()
    }

    Box(
        Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp)
    ) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
}