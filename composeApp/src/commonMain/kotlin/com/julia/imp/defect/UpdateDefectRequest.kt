package com.julia.imp.defect

import kotlinx.serialization.Serializable

@Serializable
data class UpdateDefectRequest(
    val fixed: Boolean
)