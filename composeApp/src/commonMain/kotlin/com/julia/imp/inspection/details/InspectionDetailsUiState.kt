package com.julia.imp.inspection.details

import com.julia.imp.inspection.Inspection

data class InspectionDetailsUiState(
    val loading: Boolean = true,
    val loadError: Boolean = false,
    val inspection: Inspection? = null,
    val showCosts: Boolean = false
)