package com.raya.eightraya.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "subjects",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentSubjectId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("parentSubjectId")],
)
data class SubjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val parentSubjectId: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
)
