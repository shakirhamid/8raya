package com.raya.eightraya.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raya.eightraya.data.local.entity.AiSummaryEntity
import com.raya.eightraya.data.local.entity.DocumentEntity
import com.raya.eightraya.data.local.entity.FlashcardEntity
import com.raya.eightraya.data.local.entity.McqChoiceEntity
import com.raya.eightraya.data.local.entity.McqEntity
import com.raya.eightraya.data.local.entity.SheetProgressEntity
import com.raya.eightraya.data.local.entity.SubjectEntity
import com.raya.eightraya.domain.model.DocumentType
import com.raya.eightraya.domain.model.ProcessingStatus
import java.time.Instant
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RayaDatabaseInstrumentedTest {
    private lateinit var database: RayaDatabase
    private val now: Instant = Instant.parse("2026-04-30T00:00:00Z")

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RayaDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun createsSubjectHierarchyAndObservesPdfAndMarkdownDocuments() = runTest {
        database.subjectDao().upsert(subject("subject-root", "Computer Science"))
        database.subjectDao().upsert(subject("subject-child", "Algorithms", parentId = "subject-root"))

        val children = database.subjectDao().observeChildren("subject-root").first()
        assertEquals(listOf("Algorithms"), children.map { it.name })

        database.documentDao().upsert(
            document("document-pdf", title = "Lecture 1.pdf", type = DocumentType.Pdf),
        )
        database.documentDao().upsert(
            document("document-md", title = "Sorting.md", type = DocumentType.Markdown),
        )

        val documents = database.documentDao().observeDocuments("subject-child").first()
        assertEquals(2, documents.size)
        assertEquals(setOf(DocumentType.Pdf, DocumentType.Markdown), documents.map { it.type }.toSet())
    }

    @Test
    fun persistsFlashcardsProgressAndAiStudyContent() = runTest {
        database.subjectDao().upsert(subject("subject-root", "Computer Science"))
        database.subjectDao().upsert(subject("subject-child", "Algorithms", parentId = "subject-root"))
        database.documentDao().upsert(document("document-1"))

        database.flashcardDao().upsert(
            FlashcardEntity(
                id = "flashcard-1",
                subjectId = "subject-child",
                documentId = "document-1",
                front = "What does BFS use?",
                back = "A queue.",
                createdAt = now,
                updatedAt = now,
            ),
        )
        database.progressDao().upsert(
            SheetProgressEntity(
                id = "progress-1",
                subjectId = "subject-child",
                documentId = "document-1",
                completedSheets = 7,
                totalSheets = 10,
                updatedAt = now,
            ),
        )
        database.aiContentDao().replaceDocumentContent(
            documentId = "document-1",
            summary = AiSummaryEntity(
                id = "summary-1",
                documentId = "document-1",
                overview = "Graph traversal basics",
                keyPoints = listOf("BFS uses a queue", "DFS uses a stack"),
                generatedAt = now,
            ),
            questions = listOf(
                McqEntity(
                    id = "question-1",
                    documentId = "document-1",
                    prompt = "Which structure does BFS use?",
                    correctChoiceId = "choice-b",
                    explanation = "BFS visits nodes in layers.",
                ),
            ),
            choices = listOf(
                McqChoiceEntity(
                    id = "choice-a",
                    questionId = "question-1",
                    label = "A",
                    text = "Stack",
                ),
                McqChoiceEntity(
                    id = "choice-b",
                    questionId = "question-1",
                    label = "B",
                    text = "Queue",
                ),
            ),
        )

        val flashcards = database.flashcardDao().observeFlashcardsForDocument("document-1").first()
        val progress = database.progressDao().observeProgressForDocument("document-1").first()
        val summary = database.aiContentDao().observeSummary("document-1").first()
        val questions = database.aiContentDao().observeQuestions("document-1").first()

        assertEquals("What does BFS use?", flashcards.single().front)
        assertEquals(7, progress?.completedSheets)
        assertEquals(10, progress?.totalSheets)
        assertEquals(listOf("BFS uses a queue", "DFS uses a stack"), summary?.keyPoints)
        assertEquals(2, questions.single().choices.size)
    }

    private fun subject(
        id: String,
        name: String,
        parentId: String? = null,
    ): SubjectEntity = SubjectEntity(
        id = id,
        name = name,
        parentSubjectId = parentId,
        createdAt = now,
        updatedAt = now,
    )

    private fun document(
        id: String,
        title: String = "Graphs.pdf",
        type: DocumentType = DocumentType.Pdf,
    ): DocumentEntity = DocumentEntity(
        id = id,
        subjectId = "subject-child",
        title = title,
        sourceUri = "content://$id",
        type = type,
        extractedText = "Graph traversal notes",
        processingStatus = ProcessingStatus.Completed,
        createdAt = now,
        updatedAt = now,
    )
}
