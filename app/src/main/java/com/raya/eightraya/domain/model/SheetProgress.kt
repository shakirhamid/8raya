package com.raya.eightraya.domain.model

import java.time.Instant
import java.util.UUID
import kotlin.math.max
import kotlin.math.min

data class SheetProgress(
    val id: String = UUID.randomUUID().toString(),
    val subjectId: String,
    val documentId: String? = null,
    val completedSheets: Int,
    val totalSheets: Int,
    val updatedAt: Instant = Instant.now(),
)

fun SheetProgress.completionRatio(): Float =
    if (totalSheets <= 0) 0f else completedSheets.toFloat() / totalSheets.toFloat()

fun SheetProgress.completionPercent(): Int =
    (completionRatio().coerceIn(0f, 1f) * 100).toInt()

fun SheetProgress.withCompletedSheets(value: Int): SheetProgress =
    copy(completedSheets = min(max(value, 0), max(totalSheets, 0)), updatedAt = Instant.now())
