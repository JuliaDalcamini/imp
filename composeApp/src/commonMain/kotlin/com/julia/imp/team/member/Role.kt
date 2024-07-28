package com.julia.imp.team.member

import androidx.compose.runtime.Composable
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.admin_label
import imp.composeapp.generated.resources.inspector_label
import imp.composeapp.generated.resources.viewer_label
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
enum class Role {
    @SerialName("admin") Admin,
    @SerialName("inspector") Inspector,
    @SerialName("viewer") Viewer;

    @Composable
    fun getLabel() = when (this) {
        Admin -> stringResource(Res.string.admin_label)
        Inspector -> stringResource(Res.string.inspector_label)
        Viewer -> stringResource(Res.string.viewer_label)
    }
}