package com.julia.imp.artifact.list

import com.julia.imp.common.navigation.serializableNavType
import com.julia.imp.project.Project
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class ArtifactsRoute(
    val project: Project
) {

    companion object {
        val typeMap = mapOf(typeOf<Project>() to serializableNavType<Project>())
    }
}