package com.raya.eightraya.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.raya.eightraya.data.local.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subjects WHERE parentSubjectId IS NULL ORDER BY name COLLATE NOCASE ASC")
    fun observeRootSubjects(): Flow<List<SubjectEntity>>

    @Query("SELECT * FROM subjects WHERE parentSubjectId = :parentSubjectId ORDER BY name COLLATE NOCASE ASC")
    fun observeChildren(parentSubjectId: String): Flow<List<SubjectEntity>>

    @Query("SELECT * FROM subjects WHERE id = :subjectId")
    fun observeSubject(subjectId: String): Flow<SubjectEntity?>

    @Upsert
    suspend fun upsert(subject: SubjectEntity)

    @Delete
    suspend fun delete(subject: SubjectEntity)
}
