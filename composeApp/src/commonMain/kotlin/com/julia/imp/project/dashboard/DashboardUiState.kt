package com.julia.imp.project.dashboard

import com.julia.imp.project.dashboard.data.DashboardData

data class DashboardUiState(
    val data: DashboardData? = null,
    val loading: Boolean = false,
    val error: Boolean = false
)