package com.raya.eightraya.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.raya.eightraya.domain.model.DocumentType
import com.raya.eightraya.domain.model.ProcessingStatus
import java.time.Instant

@Entity(
    tableName = "documents",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("subjectId")],
)
data class DocumentEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val title: String,
    val sourceUri: String,
    val type: DocumentType,
    val extractedText: String?,
    val processingStatus: ProcessingStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
)
