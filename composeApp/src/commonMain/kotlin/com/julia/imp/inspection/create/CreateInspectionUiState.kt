package com.julia.imp.inspection.create

import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.julia.imp.inspection.Inspection
import com.julia.imp.inspection.answer.AnswerOption
import com.julia.imp.question.Question
import kotlinx.datetime.Instant

data class CreateInspectionUiState(
    val loading: Boolean = true,
    val artifactName: String = "",
    val questions: SnapshotStateMap<Question, QuestionState>? = null,
    val startTime: Instant? = null,
    val saving: Boolean = false,
    val createdInspection: Inspection? = null,
    val canCreate: Boolean = false,
    val createError: Boolean = false,
    val loadError: Boolean = false
)