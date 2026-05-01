package com.raya.eightraya.data.repository

import com.raya.eightraya.data.local.dao.ProgressDao
import com.raya.eightraya.data.mapper.toDomain
import com.raya.eightraya.data.mapper.toEntity
import com.raya.eightraya.domain.model.SheetProgress
import com.raya.eightraya.domain.repository.ProgressRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomProgressRepository @Inject constructor(
    private val progressDao: ProgressDao,
) : ProgressRepository {
    override fun observeProgressForSubject(subjectId: String): Flow<List<SheetProgress>> =
        progressDao.observeProgressForSubject(subjectId).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun observeProgressForDocument(documentId: String): Flow<SheetProgress?> =
        progressDao.observeProgressForDocument(documentId).map { it?.toDomain() }

    override suspend fun upsertProgress(progress: SheetProgress) {
        progressDao.upsert(progress.toEntity())
    }

    override suspend fun deleteProgress(progress: SheetProgress) {
        progressDao.delete(progress.toEntity())
    }
}
