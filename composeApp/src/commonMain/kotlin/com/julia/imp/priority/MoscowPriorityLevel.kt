package com.julia.imp.priority

import androidx.compose.runtime.Composable
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.priority_moscow_could_have
import imp.composeapp.generated.resources.priority_moscow_must_have
import imp.composeapp.generated.resources.priority_moscow_should_have
import imp.composeapp.generated.resources.priority_moscow_wont_have
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
enum class MoscowPriorityLevel {
    @SerialName("wontHave") WontHave,
    @SerialName("couldHave") CouldHave,
    @SerialName("shouldHave") ShouldHave,
    @SerialName("mustHave") MustHave;

    @Composable
    fun getLabel() = when (this) {
        WontHave -> stringResource(Res.string.priority_moscow_wont_have)
        CouldHave -> stringResource(Res.string.priority_moscow_could_have)
        ShouldHave -> stringResource(Res.string.priority_moscow_should_have)
        MustHave -> stringResource(Res.string.priority_moscow_must_have)
    }
}