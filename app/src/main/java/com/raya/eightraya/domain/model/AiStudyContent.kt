package com.raya.eightraya.domain.model

import java.time.Instant
import java.util.UUID

data class AiStudySummary(
    val id: String = UUID.randomUUID().toString(),
    val documentId: String? = null,
    val overview: String,
    val keyPoints: List<String>,
    val generatedAt: Instant = Instant.now(),
)

data class MultipleChoiceQuestion(
    val id: String = UUID.randomUUID().toString(),
    val documentId: String? = null,
    val prompt: String,
    val choices: List<McqChoice>,
    val correctChoiceId: String,
    val explanation: String,
)

data class McqChoice(
    val id: String = UUID.randomUUID().toString(),
    val questionId: String? = null,
    val label: String,
    val text: String,
)

data class StudyContentResult(
    val summary: AiStudySummary,
    val questions: List<MultipleChoiceQuestion>,
)
