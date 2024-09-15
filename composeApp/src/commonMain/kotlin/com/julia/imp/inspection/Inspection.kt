package com.julia.imp.inspection

import com.julia.imp.inspection.answer.InspectionAnswer
import com.julia.imp.user.User
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class Inspection(
    val id: String,
    val inspector: User,
    val duration: Duration,
    val createdAt: Instant,
    val answers: List<InspectionAnswer>,
    val cost: Double,
    val artifactVersion: String
)