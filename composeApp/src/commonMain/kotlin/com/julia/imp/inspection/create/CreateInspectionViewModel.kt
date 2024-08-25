package com.julia.imp.inspection.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.artifact.Artifact
import com.julia.imp.inspection.InspectionRepository
import com.julia.imp.inspection.answer.AnswerOption
import com.julia.imp.question.Question
import com.julia.imp.question.QuestionRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.time.Duration

class CreateInspectionViewModel(
    private val repository: InspectionRepository = InspectionRepository(),
    private val questionRepository: QuestionRepository = QuestionRepository()
) : ViewModel() {

    private lateinit var artifact: Artifact

    var uiState by mutableStateOf(CreateInspectionUiState())
        private set

    fun initialize(artifact: Artifact) {
        this.artifact = artifact
        loadQuestions(artifact.type.id)
    }

    private fun loadQuestions(artifactTypeId: String) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, questions = null)

            try {
                val questions = questionRepository.getQuestions(artifactTypeId)

                uiState = uiState.copy(
                    loading = false,
                    questions = createAnswerMap(questions),
                    startTime = Clock.System.now()
                )
            } catch (error: Throwable) {
                uiState = uiState.copy(loadError = true)
            }
        }
    }

    private fun createAnswerMap(questions: List<Question>): SnapshotStateMap<Question, AnswerOption?> {
        val answerMap = mutableStateMapOf<Question, AnswerOption?>()

        questions.forEach { question ->
            answerMap[question] = null
        }

        return answerMap
    }

    fun setQuestionAnswer(question: Question, answer: AnswerOption) {
        val questions = uiState.questions ?: throw IllegalStateException("Questions not loaded")

        questions[question] = answer
        updateCreateButtonState()
    }

    fun createInspection() {
        viewModelScope.launch {
            uiState = uiState.copy(saving = true)

            try {
                val inspection = repository.createInspection(
                    projectId = artifact.projectId,
                    artifactId = artifact.id,
                    duration = getDuration(),
                    answers = getFinalAnswerMap()
                )

                uiState = uiState.copy(createdInspection = inspection)
            } catch (error: Throwable) {
                uiState = uiState.copy(createError = true)
            } finally {
                uiState = uiState.copy(saving = false)
            }
        }
    }

    private fun getDuration(): Duration {
        val startTime = uiState.startTime ?: throw IllegalStateException("Start time not set")
        return Clock.System.now() - startTime
    }

    private fun getFinalAnswerMap(): Map<String, AnswerOption> {
        val questions = uiState.questions ?: throw IllegalStateException("Questions not loaded")

        return questions.map { entry ->
            val question = entry.key
            val answer = entry.value ?: throw IllegalStateException("Answer not set")

            question.id to answer
        }.toMap()
    }

    fun dismissCreateError() {
        uiState = uiState.copy(createError = false)
    }

    fun dismissLoadError() {
        uiState = uiState.copy(createError = false)
    }

    private fun updateCreateButtonState() {
        uiState = uiState.copy(
            canCreate = uiState.questions?.all { it.value != null } ?: false
        )
    }
}
