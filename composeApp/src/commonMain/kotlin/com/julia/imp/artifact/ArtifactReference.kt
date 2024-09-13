package com.julia.imp.artifact

import kotlinx.serialization.Serializable

@Serializable
data class ArtifactReference(
    val id: String,
    val name: String
)