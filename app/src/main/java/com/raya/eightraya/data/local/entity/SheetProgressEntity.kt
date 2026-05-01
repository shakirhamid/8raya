package com.raya.eightraya.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "sheet_progress",
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
    indices = [
        Index("subjectId"),
        Index("documentId", unique = true),
    ],
)
data class SheetProgressEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val documentId: String?,
    val completedSheets: Int,
    val totalSheets: Int,
    val updatedAt: Instant,
)
