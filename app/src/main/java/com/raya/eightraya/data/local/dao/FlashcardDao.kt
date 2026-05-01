package com.raya.eightraya.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.raya.eightraya.data.local.entity.FlashcardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcards WHERE subjectId = :subjectId ORDER BY updatedAt DESC")
    fun observeFlashcardsForSubject(subjectId: String): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards WHERE documentId = :documentId ORDER BY updatedAt DESC")
    fun observeFlashcardsForDocument(documentId: String): Flow<List<FlashcardEntity>>

    @Upsert
    suspend fun upsert(flashcard: FlashcardEntity)

    @Delete
    suspend fun delete(flashcard: FlashcardEntity)
}
