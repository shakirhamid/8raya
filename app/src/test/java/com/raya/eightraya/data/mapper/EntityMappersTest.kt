package com.raya.eightraya.data.mapper

import com.raya.eightraya.domain.model.AiStudySummary
import com.raya.eightraya.domain.model.DocumentType
import com.raya.eightraya.domain.model.Flashcard
import com.raya.eightraya.domain.model.McqChoice
import com.raya.eightraya.domain.model.MultipleChoiceQuestion
import com.raya.eightraya.domain.model.ProcessingStatus
import com.raya.eightraya.domain.model.SheetProgress
import com.raya.eightraya.domain.model.StudyContentResult
import com.raya.eightraya.domain.model.StudyDocument
import com.raya.eightraya.domain.model.Subject
import com.raya.eightraya.domain.model.completionPercent
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EntityMappersTest {
    private val now: Instant = Instant.parse("2026-04-30T00:00:00Z")

    @Test
    fun subjectRoundTripPreservesHierarchyFields() {
        val subject = Subject(
            id = "subject-child",
            name = "Algorithms",
            parentSubjectId = "subject-root",
            createdAt = now,
            updatedAt = now,
        )

        assertEquals(subject, subject.toEntity().toDomain())
    }

    @Test
    fun documentRoundTripPreservesTypeAndProcessingStatus() {
        val document = StudyDocument(
            id = "document-1",
            subjectId = "subject-1",
            title = "Graphs.md",
            sourceUri = "content://graphs",
            type = DocumentType.Markdown,
            extractedText = "A graph is a set of vertices and edges.",
            processingStatus = ProcessingStatus.Failed("LLM quota exceeded"),
            createdAt = now,
            updatedAt = now,
        )

        assertEquals(document, document.toEntity().toDomain())
    }

    @Test
    fun flashcardAndProgressRoundTripsPreserveDocumentLinks() {
        val flashcard = Flashcard(
            id = "flashcard-1",
            subjectId = "subject-1",
            documentId = "document-1",
            front = "What is a graph?",
            back = "A collection of vertices and edges.",
            createdAt = now,
            updatedAt = now,
        )
        val progress = SheetProgress(
            id = "progress-1",
            subjectId = "subject-1",
            documentId = "document-1",
            completedSheets = 4,
            totalSheets = 8,
            updatedAt = now,
        )

        assertEquals(flashcard, flashcard.toEntity().toDomain())
        assertEquals(progress, progress.toEntity().toDomain())
        assertEquals(50, progress.completionPercent())
    }

    @Test
    fun aiContentBundleUsesExplicitDocumentIdWithoutMaps() {
        val choiceA = McqChoice(id = "choice-a", label = "A", text = "Stack")
        val choiceB = McqChoice(id = "choice-b", label = "B", text = "Queue")
        val question = MultipleChoiceQuestion(
            id = "question-1",
            prompt = "Which structure is FIFO?",
            choices = listOf(choiceA, choiceB),
            correctChoiceId = "choice-b",
            explanation = "A queue removes items in insertion order.",
        )
        val result = StudyContentResult(
            summary = AiStudySummary(
                id = "summary-1",
                overview = "Data structures overview",
                keyPoints = listOf("Stacks are LIFO", "Queues are FIFO"),
                generatedAt = now,
            ),
            questions = listOf(question),
        )

        val bundle = result.toEntityBundle(documentId = "document-1")

        assertEquals("document-1", bundle.summary.documentId)
        assertEquals("document-1", bundle.questions.single().documentId)
        assertTrue(bundle.choices.all { it.questionId == "question-1" })
    }
}
