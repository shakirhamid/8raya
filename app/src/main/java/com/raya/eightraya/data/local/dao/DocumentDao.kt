package com.raya.eightraya.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.raya.eightraya.data.local.entity.DocumentEntity
import com.raya.eightraya.domain.model.ProcessingStatus
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents WHERE subjectId = :subjectId ORDER BY updatedAt DESC")
    fun observeDocuments(subjectId: String): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM documents WHERE id = :documentId")
    fun observeDocument(documentId: String): Flow<DocumentEntity?>

    @Upsert
    suspend fun upsert(document: DocumentEntity)

    @Query("UPDATE documents SET processingStatus = :status, updatedAt = :updatedAt WHERE id = :documentId")
    suspend fun updateProcessingStatus(
        documentId: String,
        status: ProcessingStatus,
        updatedAt: Instant = Instant.now(),
    )

    @Delete
    suspend fun delete(document: DocumentEntity)
}
