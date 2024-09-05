package com.julia.imp.project.manage

import com.julia.imp.common.navigation.serializableNavType
import com.julia.imp.project.Project
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class ManageProjectRoute(
    val project: Project
) {

    companion object {
        val typeMap = mapOf(typeOf<Project>() to serializableNavType<Project>())
    }
}