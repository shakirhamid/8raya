package com.raya.eightraya.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.raya.eightraya.data.local.entity.SheetProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM sheet_progress WHERE subjectId = :subjectId ORDER BY updatedAt DESC")
    fun observeProgressForSubject(subjectId: String): Flow<List<SheetProgressEntity>>

    @Query("SELECT * FROM sheet_progress WHERE documentId = :documentId")
    fun observeProgressForDocument(documentId: String): Flow<SheetProgressEntity?>

    @Upsert
    suspend fun upsert(progress: SheetProgressEntity)

    @Delete
    suspend fun delete(progress: SheetProgressEntity)
}
