package com.raya.eightraya.ui.subjectdetails

sealed interface SubjectDetailsUiState {
    data object Loading : SubjectDetailsUiState

    data class Content(
        val subjectName: String,
        val documents: List<DocumentListItemUiState>,
    ) : SubjectDetailsUiState

    data object SubjectNotFound : SubjectDetailsUiState

    data class Error(
        val message: String,
    ) : SubjectDetailsUiState
}

data class DocumentListItemUiState(
    val id: String,
    val title: String,
    val typeLabel: String,
    val statusLabel: String,
    val extractedText: String?,
)

data class SummarySheetUiState(
    val documentTitle: String,
    val phase: SummarySheetPhase,
)

sealed interface SummarySheetPhase {
    data object Loading : SummarySheetPhase

    data class Success(
        val summaryText: String,
    ) : SummarySheetPhase

    data class Error(
        val message: String,
    ) : SummarySheetPhase
}
