package com.raya.eightraya.ui.subjectdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.raya.eightraya.domain.model.DocumentType
import com.raya.eightraya.domain.model.ProcessingStatus
import com.raya.eightraya.domain.model.StudyDocument
import com.raya.eightraya.domain.repository.DocumentRepository
import com.raya.eightraya.domain.repository.SubjectRepository
import com.raya.eightraya.ui.navigation.SubjectDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class SubjectDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val subjectRepository: SubjectRepository,
    private val documentRepository: DocumentRepository,
) : ViewModel() {
    private val subjectId: String = savedStateHandle.toRoute<SubjectDetailsDestination>().subjectId

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
}

private fun List<StudyDocument>.toDocumentListItems(): List<DocumentListItemUiState> =
    map { document ->
        DocumentListItemUiState(
            id = document.id,
            title = document.title,
            typeLabel = document.type.toDisplayLabel(),
            statusLabel = document.processingStatus.toDisplayLabel(),
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
