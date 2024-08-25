package com.julia.imp.inspection.create

import com.julia.imp.artifact.Artifact
import com.julia.imp.common.navigation.serializableNavType
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class CreateInspectionRoute(
    val artifact: Artifact
) {

    companion object {
        val typeMap = mapOf(typeOf<Artifact>() to serializableNavType<Artifact>())
    }
}