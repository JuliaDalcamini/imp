package com.julia.imp.defect

data class DefectsUiState(
    val loading: Boolean = false,
    val filter: DefectFilter = DefectFilter.NotFixed,
    val defects: List<Defect>? = null,
    val error: Boolean = false,
    val showFixButton: Boolean = false,
    val actionError: Boolean = false
)