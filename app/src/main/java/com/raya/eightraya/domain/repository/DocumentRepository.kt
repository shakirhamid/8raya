package com.raya.eightraya.domain.repository

import com.raya.eightraya.domain.model.ProcessingStatus
import com.raya.eightraya.domain.model.StudyDocument
import kotlinx.coroutines.flow.Flow

interface DocumentRepository {
    fun observeDocuments(subjectId: String): Flow<List<StudyDocument>>
    fun observeDocument(documentId: String): Flow<StudyDocument?>
    suspend fun upsertDocument(document: StudyDocument)
    suspend fun updateProcessingStatus(documentId: String, status: ProcessingStatus)
    suspend fun deleteDocument(document: StudyDocument)
}
