package com.raya.eightraya.data.repository

import com.raya.eightraya.data.local.dao.SubjectDao
import com.raya.eightraya.data.mapper.toDomain
import com.raya.eightraya.data.mapper.toEntity
import com.raya.eightraya.domain.model.Subject
import com.raya.eightraya.domain.repository.SubjectRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomSubjectRepository @Inject constructor(
    private val subjectDao: SubjectDao,
) : SubjectRepository {
    override fun observeRootSubjects(): Flow<List<Subject>> =
        subjectDao.observeRootSubjects().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun observeChildren(parentSubjectId: String): Flow<List<Subject>> =
        subjectDao.observeChildren(parentSubjectId).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun observeSubject(subjectId: String): Flow<Subject?> =
        subjectDao.observeSubject(subjectId).map { it?.toDomain() }

    override suspend fun upsertSubject(subject: Subject) {
        subjectDao.upsert(subject.toEntity())
    }

    override suspend fun deleteSubject(subject: Subject) {
        subjectDao.delete(subject.toEntity())
    }
}
