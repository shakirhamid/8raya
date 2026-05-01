package com.raya.eightraya.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mcq_choices",
    foreignKeys = [
        ForeignKey(
            entity = McqEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("questionId")],
)
data class McqChoiceEntity(
    @PrimaryKey val id: String,
    val questionId: String,
    val label: String,
    val text: String,
)
