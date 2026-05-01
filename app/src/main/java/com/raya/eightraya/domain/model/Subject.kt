package com.raya.eightraya.domain.model

import java.time.Instant
import java.util.UUID

data class Subject(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val parentSubjectId: String? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = createdAt,
)

fun Subject.isRoot(): Boolean = parentSubjectId == null
