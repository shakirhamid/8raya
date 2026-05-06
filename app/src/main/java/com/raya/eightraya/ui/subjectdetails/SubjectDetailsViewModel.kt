package com.raya.eightraya.ui.subjectdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.raya.eightraya.data.ai.GeminiSummaryService
import com.raya.eightraya.domain.model.DocumentType
import com.raya.eightraya.domain.model.ProcessingStatus
import com.raya.eightraya.domain.model.StudyDocument
import com.raya.eightraya.domain.repository.DocumentRepository
import com.raya.eightraya.domain.repository.SubjectRepository
import com.raya.eightraya.ui.navigation.SubjectDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SubjectDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val subjectRepository: SubjectRepository,
    private val documentRepository: DocumentRepository,
    private val geminiSummaryService: GeminiSummaryService,
) : ViewModel() {
    private val subjectId: String = savedStateHandle.toRoute<SubjectDetailsDestination>().subjectId

    private val _summarySheet = MutableStateFlow<SummarySheetUiState?>(null)
    val summarySheet: StateFlow<SummarySheetUiState?> = _summarySheet.asStateFlow()

    val uiState: StateFlow<SubjectDetailsUiState> =
        combine(
            subjectRepository.observeSubject(subjectId),
            documentRepository.observeDocuments(subjectId),
        ) { subject, documents ->
            when (subject) {
                null -> SubjectDetailsUiState.SubjectNotFound
                else ->
                    SubjectDetailsUiState.Content(
                        subjectName = subject.name,
                        documents = documents.toDocumentListItems(),
                    )
            }
        }
            .catch { throwable ->
                val message = throwable.message ?: "Could not load subject details."
                emit(SubjectDetailsUiState.Error(message))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = SubjectDetailsUiState.Loading,
            )

    fun requestDocumentSummary(documentTitle: String, extractedText: String?) {
        if (extractedText.isNullOrBlank()) {
            _summarySheet.value = SummarySheetUiState(
                documentTitle = documentTitle,
                phase = SummarySheetPhase.Error(
                    "No text is available for this document yet. Add extracted text before summarizing.",
                ),
            )
            return
        }
        _summarySheet.value = SummarySheetUiState(
            documentTitle = documentTitle,
            phase = SummarySheetPhase.Loading,
        )
        summarizeDocument(extractedText.trim())
    }

    /**
     * Generates a concise summary of [text] using Gemini 1.5 Flash and updates the summary bottom sheet.
     */
    fun summarizeDocument(text: String) {
        viewModelScope.launch {
            val sheet = _summarySheet.value ?: return@launch
            val title = sheet.documentTitle
            runCatching { geminiSummaryService.generateConciseSummary(text) }.fold(
                onSuccess = { summary ->
                    _summarySheet.value = SummarySheetUiState(
                        documentTitle = title,
                        phase = SummarySheetPhase.Success(summaryText = summary),
                    )
                },
                onFailure = { throwable ->
                    val message = throwable.message ?: "Summary could not be generated."
                    _summarySheet.value = SummarySheetUiState(
                        documentTitle = title,
                        phase = SummarySheetPhase.Error(message = message),
                    )
                },
            )
        }
    }

    fun clearSummarySheet() {
        _summarySheet.value = null
    }
}

private fun List<StudyDocument>.toDocumentListItems(): List<DocumentListItemUiState> =
    map { document ->
        DocumentListItemUiState(
            id = document.id,
            title = document.title,
            typeLabel = document.type.toDisplayLabel(),
            statusLabel = document.processingStatus.toDisplayLabel(),
            extractedText = document.extractedText,
        )
    }

private fun DocumentType.toDisplayLabel(): String =
    when (this) {
        DocumentType.Pdf -> "PDF"
        DocumentType.Markdown -> "Markdown"
    }

private fun ProcessingStatus.toDisplayLabel(): String =
    when (this) {
        ProcessingStatus.Pending -> "Pending"
        ProcessingStatus.Processing -> "Processing"
        ProcessingStatus.Completed -> "Completed"
        is ProcessingStatus.Failed -> "Failed: ${this.reason}"
    }
