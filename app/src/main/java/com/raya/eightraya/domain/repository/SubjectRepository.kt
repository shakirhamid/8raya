package com.raya.eightraya.domain.repository

import com.raya.eightraya.domain.model.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {
    fun observeRootSubjects(): Flow<List<Subject>>
    fun observeChildren(parentSubjectId: String): Flow<List<Subject>>
    fun observeSubject(subjectId: String): Flow<Subject?>
    suspend fun upsertSubject(subject: Subject)
    suspend fun deleteSubject(subject: Subject)
}
