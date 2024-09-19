package com.julia.imp.artifact.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.common.session.requireSession
import com.julia.imp.common.session.requireTeam
import com.julia.imp.inspection.Inspection
import com.julia.imp.inspection.InspectionRepository
import com.julia.imp.project.Project
import com.julia.imp.project.ProjectRepository
import com.julia.imp.team.inspector.InspectorRepository
import com.julia.imp.user.User
import kotlinx.coroutines.launch

class ArtifactDetailsViewModel(
    private val artifactRepository: ArtifactRepository = ArtifactRepository(),
    private val projectRepository: ProjectRepository = ProjectRepository(),
    private val inspectionRepository: InspectionRepository = InspectionRepository(),
    private val inspectorRepository: InspectorRepository = InspectorRepository()
) : ViewModel() {

    private lateinit var artifactId: String
    private lateinit var project: Project

    var uiState by mutableStateOf(ArtifactDetailsUiState())
        private set

    suspend fun initialize(artifactId: String, projectId: String) {
        this.artifactId = artifactId
        this.project = projectRepository.getProject(projectId)
        loadArtifact()
    }

    private fun updateInspectButtonState() {
        uiState = uiState.copy(
            canInspect = uiState.artifact?.archived == false &&
                    isUserInInspectorList() &&
                    !project.finished &&
                    uiState.artifact?.priority != null
        )
    }

    private fun isUserInInspectorList(): Boolean =
        uiState.artifact?.inspectors?.any { it.id == requireSession().userId } ?: false

    private fun loadArtifact() {
        viewModelScope.launch {
            uiState = ArtifactDetailsUiState(loading = true)

            try {
                val artifact = artifactRepository.getArtifact(
                    projectId = project.id,
                    artifactId = artifactId
                )

                val inspections = inspectionRepository.getInspections(
                    projectId = artifact.projectId,
                    artifactId = artifact.id
                )

                onArtifactLoaded(artifact, inspections)
            } catch (error: Throwable) {
                uiState = uiState.copy(loadError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    private fun onArtifactLoaded(artifact: Artifact, inspections: List<Inspection>?) {
        val isAdmin = requireSession().isTeamAdmin
        val isInspector = requireSession().isInspector
        val filteredInspections = inspections?.filter { it.inspector.id == requireSession().userId }

        val showReinspectAlert = !artifact.archived &&
                !artifact.fullyInspected &&
                !filteredInspections.isNullOrEmpty() &&
                filteredInspections.none { it.artifactVersion == artifact.currentVersion }

        uiState = uiState.copy(
            artifact = artifact,
            inspections = if (isInspector) filteredInspections else inspections,
            lastInspection = inspections?.maxOfOrNull { it.createdAt },
            canEditInspectors = isAdmin && !project.finished,
            showEditButton = isAdmin && !artifact.archived && !project.finished,
            showCosts = !isInspector,
            showReinspectAlert = showReinspectAlert
        )

        updateInspectButtonState()
    }

    fun dismissLoadError() {
        uiState = uiState.copy(loadError = false)
    }

    fun dismissActionError() {
        uiState = uiState.copy(actionError = false)
    }

    fun addInspector(inspector: User) {
        uiState.artifact?.let { artifact ->
            updateInspectors(artifact.inspectors + inspector)
        }
    }

    fun removeInspector(inspector: User) {
        uiState.artifact?.let { artifact ->
            updateInspectors(artifact.inspectors - inspector)
        }
    }

    private fun updateInspectors(inspectors: List<User>) {
        val artifact = uiState.artifact ?: return

        viewModelScope.launch {
            uiState = uiState.copy(updatingInspectors = true)

            try {
                val updatedArtifact = artifactRepository.updateArtifact(
                    artifact.copy(inspectors = inspectors)
                )

                onArtifactLoaded(updatedArtifact, uiState.inspections)
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            } finally {
                uiState = uiState.copy(updatingInspectors = false)
            }
        }
    }

    fun openInspectorPicker() {
        uiState = uiState.copy(
            showInspectorPicker = true,
            availableInspectors = null
        )

        loadAvailableInspectors()
    }

    private fun loadAvailableInspectors() {
        viewModelScope.launch {
            uiState = uiState.copy(availableInspectors = null)

            try {
                val inspectors = inspectorRepository.getInspectors(requireTeam().id)
                val currentInspectors = uiState.artifact?.inspectors.orEmpty().toSet()

                uiState = uiState.copy(availableInspectors = inspectors - currentInspectors)
            } catch (error: Throwable) {
                uiState = uiState.copy(loadError = true, showInspectorPicker = false)
            }
        }
    }

    fun dismissInspectorPicker() {
        uiState = uiState.copy(
            showInspectorPicker = false,
            availableInspectors = null
        )
    }
}
