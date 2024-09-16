package com.julia.imp.project.dashboard.data

import kotlinx.serialization.Serializable

@Serializable
data class DashboardData(
    val inspectionProgress: Progress,
    val defectsProgress: Progress,
    val effortOverview: EffortOverview,
    val costOverview: CostOverview,
    val inspectors: List<InspectorSummary>,
    val artifactTypes: List<ArtifactTypeSummary>,
    val defectTypes: List<DefectTypeSummary>
)