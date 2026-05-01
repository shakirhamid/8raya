package com.raya.eightraya.domain.model

import kotlin.math.max
import kotlin.math.min

data class SubjectProgressOverview(
    val subject: Subject,
    val completedSheets: Int,
    val totalSheets: Int,
)

fun SubjectProgressOverview.progressRatio(): Float =
    if (totalSheets <= 0) 0f else completedSheets.toFloat() / totalSheets.toFloat()

fun SubjectProgressOverview.progressPercent(): Int =
    (progressRatio().coerceIn(0f, 1f) * 100).toInt()

fun SubjectProgressOverview.normalized(): SubjectProgressOverview {
    val safeTotal = max(totalSheets, 0)
    val safeCompleted = min(max(completedSheets, 0), safeTotal)
    return copy(completedSheets = safeCompleted, totalSheets = safeTotal)
}
