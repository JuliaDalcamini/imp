package com.julia.imp.inspection.answer

import androidx.compose.runtime.Composable
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.answer_option_no_label
import imp.composeapp.generated.resources.answer_option_not_applicable_label
import imp.composeapp.generated.resources.answer_option_yes_label
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
enum class AnswerOption {
    @SerialName("yes") Yes,
    @SerialName("no") No,
    @SerialName("notApplicable") NotApplicable;

    @Composable
    fun getLabel() = when (this) {
        Yes -> stringResource(Res.string.answer_option_yes_label)
        No -> stringResource(Res.string.answer_option_no_label)
        NotApplicable -> stringResource(Res.string.answer_option_not_applicable_label)
    }
}