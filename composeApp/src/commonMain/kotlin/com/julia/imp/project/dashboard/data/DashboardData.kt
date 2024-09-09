package com.julia.imp.project.dashboard.data

import kotlinx.serialization.Serializable

@Serializable
data class DashboardData(
    val overallProgress: OverallProgress,
    val effortOverview: EffortOverview,
    val costOverview: CostOverview,
    val inspectorProgress: List<InspectorProgress>,
    val artifactTypes: List<ArtifactTypeDefectSummary>,
    val defectsTypes: List<DefectTypeSummary>
)