package com.raya.eightraya.ui.subjects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raya.eightraya.domain.model.Subject
import com.raya.eightraya.domain.model.SubjectProgressOverview
import com.raya.eightraya.domain.model.progressPercent
import com.raya.eightraya.domain.model.progressRatio
import com.raya.eightraya.domain.repository.SubjectRepository
import com.raya.eightraya.domain.usecase.ObserveSubjectProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class SubjectsViewModel @Inject constructor(
    observeSubjectProgress: ObserveSubjectProgressUseCase,
    private val subjectRepository: SubjectRepository,
) : ViewModel() {
    private val _events = MutableSharedFlow<SubjectsUiEvent>()
    val events = _events.asSharedFlow()

    val uiState: StateFlow<SubjectsUiState> = observeSubjectProgress()
        .map { overviews -> overviews.toUiState() }
        .onStart { emit(SubjectsUiState.Loading) }
        .catch { throwable ->
            val message = throwable.message ?: "Subjects could not be loaded."
            _events.emit(SubjectsUiEvent.LoadFailed(message))
            emit(SubjectsUiState.Error(message))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = SubjectsUiState.Loading,
        )

    fun createSubject(name: String) {
        val trimmedName = name.trim()
        if (trimmedName.isBlank()) {
            return
        }

        viewModelScope.launch {
            runCatching {
                subjectRepository.upsertSubject(Subject(name = trimmedName))
            }.onSuccess {
                _events.emit(SubjectsUiEvent.SubjectCreated(trimmedName))
            }.onFailure { throwable ->
                _events.emit(
                    SubjectsUiEvent.SaveFailed(
                        throwable.message ?: "Subject could not be saved.",
                    ),
                )
            }
        }
    }

    private fun List<SubjectProgressOverview>.toUiState(): SubjectsUiState {
        val totalCompleted = sumOf { it.completedSheets }
        val totalSheets = sumOf { it.totalSheets }
        val overallProgress = if (totalSheets <= 0) 0f else totalCompleted.toFloat() / totalSheets

        return SubjectsUiState.Content(
            subjects = map { overview ->
                SubjectCardUiState(
                    id = overview.subject.id,
                    name = overview.subject.name,
                    completedSheets = overview.completedSheets,
                    totalSheets = overview.totalSheets,
                    progress = overview.progressRatio().coerceIn(0f, 1f),
                    progressLabel = "${overview.progressPercent()}%",
                )
            },
            overallProgress = overallProgress.coerceIn(0f, 1f),
        )
    }
}
