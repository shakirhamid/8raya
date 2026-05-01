package com.raya.eightraya.domain.model

import java.time.Instant
import java.util.UUID

data class StudyDocument(
    val id: String = UUID.randomUUID().toString(),
    val subjectId: String,
    val title: String,
    val sourceUri: String,
    val type: DocumentType,
    val extractedText: String? = null,
    val processingStatus: ProcessingStatus = ProcessingStatus.Pending,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = createdAt,
)

fun StudyDocument.canBeProcessed(): Boolean = !extractedText.isNullOrBlank()
