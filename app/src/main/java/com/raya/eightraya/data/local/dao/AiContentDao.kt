package com.raya.eightraya.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.raya.eightraya.data.local.entity.AiSummaryEntity
import com.raya.eightraya.data.local.entity.McqChoiceEntity
import com.raya.eightraya.data.local.entity.McqEntity
import com.raya.eightraya.data.local.relation.McqWithChoices
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AiContentDao {
    @Query("SELECT * FROM ai_summaries WHERE documentId = :documentId")
    abstract fun observeSummary(documentId: String): Flow<AiSummaryEntity?>

    @Transaction
    @Query("SELECT * FROM mcqs WHERE documentId = :documentId ORDER BY prompt COLLATE NOCASE ASC")
    abstract fun observeQuestions(documentId: String): Flow<List<McqWithChoices>>

    @Upsert
    abstract suspend fun upsertSummary(summary: AiSummaryEntity)

    @Upsert
    abstract suspend fun upsertQuestions(questions: List<McqEntity>)

    @Upsert
    abstract suspend fun upsertChoices(choices: List<McqChoiceEntity>)

    @Query("DELETE FROM ai_summaries WHERE documentId = :documentId")
    abstract suspend fun deleteSummary(documentId: String)

    @Query("DELETE FROM mcqs WHERE documentId = :documentId")
    abstract suspend fun deleteQuestions(documentId: String)

    @Transaction
    open suspend fun replaceDocumentContent(
        documentId: String,
        summary: AiSummaryEntity,
        questions: List<McqEntity>,
        choices: List<McqChoiceEntity>,
    ) {
        deleteSummary(documentId)
        deleteQuestions(documentId)
        upsertSummary(summary)
        upsertQuestions(questions)
        upsertChoices(choices)
    }
}
