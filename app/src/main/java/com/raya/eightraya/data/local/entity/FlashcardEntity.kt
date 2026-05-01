package com.raya.eightraya.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "flashcards",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = DocumentEntity::class,
            parentColumns = ["id"],
            childColumns = ["documentId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [Index("subjectId"), Index("documentId")],
)
data class FlashcardEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val documentId: String?,
    val front: String,
    val back: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
