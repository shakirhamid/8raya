package com.raya.eightraya.data.repository

import com.raya.eightraya.data.local.dao.DocumentDao
import com.raya.eightraya.data.mapper.toDomain
import com.raya.eightraya.data.mapper.toEntity
import com.raya.eightraya.domain.model.ProcessingStatus
import com.raya.eightraya.domain.model.StudyDocument
import com.raya.eightraya.domain.repository.DocumentRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomDocumentRepository @Inject constructor(
    private val documentDao: DocumentDao,
) : DocumentRepository {
    override fun observeDocuments(subjectId: String): Flow<List<StudyDocument>> =
        documentDao.observeDocuments(subjectId).map { entities ->
            entities.map { entity -> entity.toDomain() }
        }

    override fun observeDocument(documentId: String): Flow<StudyDocument?> =
        documentDao.observeDocument(documentId).map { entity -> entity?.toDomain() }

    override suspend fun upsertDocument(document: StudyDocument) {
        documentDao.upsert(document.toEntity())
    }

    override suspend fun updateProcessingStatus(documentId: String, status: ProcessingStatus) {
        documentDao.updateProcessingStatus(documentId, status)
    }

    override suspend fun deleteDocument(document: StudyDocument) {
        documentDao.delete(document.toEntity())
    }
}
