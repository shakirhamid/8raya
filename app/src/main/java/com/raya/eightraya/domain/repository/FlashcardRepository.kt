package com.raya.eightraya.domain.repository

import com.raya.eightraya.domain.model.Flashcard
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    fun observeFlashcardsForSubject(subjectId: String): Flow<List<Flashcard>>
    fun observeFlashcardsForDocument(documentId: String): Flow<List<Flashcard>>
    suspend fun upsertFlashcard(flashcard: Flashcard)
    suspend fun deleteFlashcard(flashcard: Flashcard)
}
