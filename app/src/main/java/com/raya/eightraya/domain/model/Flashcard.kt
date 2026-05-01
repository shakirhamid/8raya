package com.raya.eightraya.domain.model

import java.time.Instant
import java.util.UUID

data class Flashcard(
    val id: String = UUID.randomUUID().toString(),
    val subjectId: String,
    val documentId: String? = null,
    val front: String,
    val back: String,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = createdAt,
)

fun Flashcard.belongsToDocument(): Boolean = documentId != null
