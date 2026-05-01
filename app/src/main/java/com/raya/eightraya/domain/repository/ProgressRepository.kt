package com.raya.eightraya.domain.repository

import com.raya.eightraya.domain.model.SheetProgress
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    fun observeProgressForSubject(subjectId: String): Flow<List<SheetProgress>>
    fun observeProgressForDocument(documentId: String): Flow<SheetProgress?>
    suspend fun upsertProgress(progress: SheetProgress)
    suspend fun deleteProgress(progress: SheetProgress)
}
