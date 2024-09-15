package com.julia.imp.project.dashboard.data

import kotlinx.serialization.Serializable

@Serializable
data class DashboardData(
    val inspectionProgress: Progress,
    val defectsProgress: Progress,
    val effortOverview: EffortOverview,
    val costOverview: CostOverview,
    val inspectorsProgress: List<InspectorProgress>,
    val artifactTypes: List<ArtifactTypeDefectSummary>,
    val defectTypes: List<DefectTypeSummary>
)