package com.julia.imp.priority

data class PrioritizerUiState (
    val priority: Priority? = null,
    val canPrioritize: Boolean = false,
    val prioritizeError: Boolean = false,
    val loadError: Boolean = false,
    val actionError: Boolean = false,
    val loading: Boolean = true,
)
