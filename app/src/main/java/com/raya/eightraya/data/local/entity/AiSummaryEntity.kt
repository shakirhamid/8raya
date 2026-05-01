package com.raya.eightraya.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "ai_summaries",
    foreignKeys = [
        ForeignKey(
            entity = DocumentEntity::class,
            parentColumns = ["id"],
            childColumns = ["documentId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("documentId", unique = true)],
)
data class AiSummaryEntity(
    @PrimaryKey val id: String,
    val documentId: String,
    val overview: String,
    val keyPoints: List<String>,
    val generatedAt: Instant,
)
