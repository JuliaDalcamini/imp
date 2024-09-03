package com.julia.imp.artifact.prioritize

import com.julia.imp.artifact.Artifact
import com.julia.imp.common.navigation.serializableNavType
import com.julia.imp.project.Project
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class PrioritizeArtifactRoute(
    val artifact: Artifact,
    val project: Project
) {

    companion object {
        val typeMap = mapOf(
            typeOf<Artifact>() to serializableNavType<Artifact>(),
            typeOf<Project>() to serializableNavType<Project>()
        )
    }
}