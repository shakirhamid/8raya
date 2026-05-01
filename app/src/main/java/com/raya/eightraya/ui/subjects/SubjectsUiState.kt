package com.raya.eightraya.ui.subjects

sealed interface SubjectsUiState {
    data object Loading : SubjectsUiState

    data class Content(
        val subjects: List<SubjectCardUiState>,
        val overallProgress: Float,
    ) : SubjectsUiState

    data class Error(
        val message: String,
    ) : SubjectsUiState
}

data class SubjectCardUiState(
    val id: String,
    val name: String,
    val completedSheets: Int,
    val totalSheets: Int,
    val progress: Float,
    val progressLabel: String,
)

sealed interface SubjectsUiEvent {
    data class LoadFailed(val message: String) : SubjectsUiEvent
    data class SaveFailed(val message: String) : SubjectsUiEvent
    data class SubjectCreated(val name: String) : SubjectsUiEvent
}
