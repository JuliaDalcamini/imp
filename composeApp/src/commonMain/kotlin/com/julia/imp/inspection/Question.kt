package com.julia.imp.inspection

import kotlinx.serialization.Serializable

// TODO: Move to proper package
@Serializable
data class Question(
    val id: String,
    val text: String,
    // TODO: Create and use Severity enum
    val severity: String,
    // TODO: Create and use DefectType class
    val defectTypeId: String
)