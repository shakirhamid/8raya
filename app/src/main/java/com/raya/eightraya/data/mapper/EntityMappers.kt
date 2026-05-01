package com.raya.eightraya.data.mapper

import com.raya.eightraya.data.local.entity.AiSummaryEntity
import com.raya.eightraya.data.local.entity.DocumentEntity
import com.raya.eightraya.data.local.entity.FlashcardEntity
import com.raya.eightraya.data.local.entity.McqChoiceEntity
import com.raya.eightraya.data.local.entity.McqEntity
import com.raya.eightraya.data.local.entity.SheetProgressEntity
import com.raya.eightraya.data.local.entity.SubjectEntity
import com.raya.eightraya.data.local.relation.McqWithChoices
import com.raya.eightraya.domain.model.AiStudySummary
import com.raya.eightraya.domain.model.Flashcard
import com.raya.eightraya.domain.model.McqChoice
import com.raya.eightraya.domain.model.MultipleChoiceQuestion
import com.raya.eightraya.domain.model.SheetProgress
import com.raya.eightraya.domain.model.StudyContentResult
import com.raya.eightraya.domain.model.StudyDocument
import com.raya.eightraya.domain.model.Subject

data class AiContentEntityBundle(
    val summary: AiSummaryEntity,
    val questions: List<McqEntity>,
    val choices: List<McqChoiceEntity>,
)

fun SubjectEntity.toDomain(): Subject = Subject(
    id = id,
    name = name,
    parentSubjectId = parentSubjectId,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun Subject.toEntity(): SubjectEntity = SubjectEntity(
    id = id,
    name = name,
    parentSubjectId = parentSubjectId,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun DocumentEntity.toDomain(): StudyDocument = StudyDocument(
    id = id,
    subjectId = subjectId,
    title = title,
    sourceUri = sourceUri,
    type = type,
    extractedText = extractedText,
    processingStatus = processingStatus,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun StudyDocument.toEntity(): DocumentEntity = DocumentEntity(
    id = id,
    subjectId = subjectId,
    title = title,
    sourceUri = sourceUri,
    type = type,
    extractedText = extractedText,
    processingStatus = processingStatus,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun FlashcardEntity.toDomain(): Flashcard = Flashcard(
    id = id,
    subjectId = subjectId,
    documentId = documentId,
    front = front,
    back = back,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun Flashcard.toEntity(): FlashcardEntity = FlashcardEntity(
    id = id,
    subjectId = subjectId,
    documentId = documentId,
    front = front,
    back = back,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun SheetProgressEntity.toDomain(): SheetProgress = SheetProgress(
    id = id,
    subjectId = subjectId,
    documentId = documentId,
    completedSheets = completedSheets,
    totalSheets = totalSheets,
    updatedAt = updatedAt,
)

fun SheetProgress.toEntity(): SheetProgressEntity = SheetProgressEntity(
    id = id,
    subjectId = subjectId,
    documentId = documentId,
    completedSheets = completedSheets,
    totalSheets = totalSheets,
    updatedAt = updatedAt,
)

fun AiSummaryEntity.toDomain(): AiStudySummary = AiStudySummary(
    id = id,
    documentId = documentId,
    overview = overview,
    keyPoints = keyPoints,
    generatedAt = generatedAt,
)

fun AiStudySummary.toEntity(documentId: String): AiSummaryEntity = AiSummaryEntity(
    id = id,
    documentId = documentId,
    overview = overview,
    keyPoints = keyPoints,
    generatedAt = generatedAt,
)

fun McqChoiceEntity.toDomain(): McqChoice = McqChoice(
    id = id,
    questionId = questionId,
    label = label,
    text = text,
)

fun McqChoice.toEntity(questionId: String): McqChoiceEntity = McqChoiceEntity(
    id = id,
    questionId = questionId,
    label = label,
    text = text,
)

fun McqWithChoices.toDomain(): MultipleChoiceQuestion = MultipleChoiceQuestion(
    id = question.id,
    documentId = question.documentId,
    prompt = question.prompt,
    choices = choices.map(McqChoiceEntity::toDomain),
    correctChoiceId = question.correctChoiceId,
    explanation = question.explanation,
)

fun MultipleChoiceQuestion.toEntity(documentId: String): McqEntity = McqEntity(
    id = id,
    documentId = documentId,
    prompt = prompt,
    correctChoiceId = correctChoiceId,
    explanation = explanation,
)

fun StudyContentResult.toEntityBundle(documentId: String): AiContentEntityBundle =
    AiContentEntityBundle(
        summary = summary.toEntity(documentId),
        questions = questions.map { it.toEntity(documentId) },
        choices = questions.flatMap { question ->
            question.choices.map { choice -> choice.toEntity(question.id) }
        },
    )
