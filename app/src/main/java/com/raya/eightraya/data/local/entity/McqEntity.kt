package com.raya.eightraya.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mcqs",
    foreignKeys = [
        ForeignKey(
            entity = DocumentEntity::class,
            parentColumns = ["id"],
            childColumns = ["documentId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("documentId")],
)
data class McqEntity(
    @PrimaryKey val id: String,
    val documentId: String,
    val prompt: String,
    val correctChoiceId: String,
    val explanation: String,
)
