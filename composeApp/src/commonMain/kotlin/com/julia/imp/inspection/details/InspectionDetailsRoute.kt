package com.julia.imp.inspection.details

import com.julia.imp.artifact.Artifact
import com.julia.imp.common.navigation.serializableNavType
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class InspectionDetailsRoute(
    val artifact: Artifact,
    val projectId: String,
    val inspectionId: String
) {

    companion object {
        val typeMap = mapOf(typeOf<Artifact>() to serializableNavType<Artifact>())
    }
}