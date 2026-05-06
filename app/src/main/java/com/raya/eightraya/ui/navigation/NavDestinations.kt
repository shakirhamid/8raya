package com.raya.eightraya.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object SubjectsDestination

@Serializable
data class SubjectDetailsDestination(
    val subjectId: String,
)
