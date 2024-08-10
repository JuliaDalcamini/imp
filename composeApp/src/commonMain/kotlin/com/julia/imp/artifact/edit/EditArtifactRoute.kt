package com.julia.imp.artifact.edit

import com.julia.imp.artifact.Artifact
import com.julia.imp.common.navigation.serializableNavType
import com.julia.imp.project.Project
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class EditArtifactRoute(
    val project: Project,
    val artifact: Artifact
) {

    companion object {
        val typeMap = mapOf(
            typeOf<Project>() to serializableNavType<Project>(),
            typeOf<Artifact>() to serializableNavType<Artifact>()
        )
    }
}