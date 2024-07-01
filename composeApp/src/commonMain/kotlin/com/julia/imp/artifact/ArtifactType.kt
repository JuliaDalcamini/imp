package com.julia.imp.artifact

import kotlinx.serialization.Serializable

@Serializable
data class ArtifactType(
    val id: String,
    val name: String
)