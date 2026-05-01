package com.raya.eightraya.domain.usecase

import com.raya.eightraya.domain.model.Subject
import com.raya.eightraya.domain.model.SubjectProgressOverview
import com.raya.eightraya.domain.model.normalized
import com.raya.eightraya.domain.repository.ProgressRepository
import com.raya.eightraya.domain.repository.SubjectRepository
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class ObserveSubjectProgressUseCase @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val progressRepository: ProgressRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<SubjectProgressOverview>> =
        subjectRepository.observeRootSubjects().flatMapLatest { subjects ->
            if (subjects.isEmpty()) {
                flowOf(emptyList<SubjectProgressOverview>())
            } else {
                combine(subjects.map(::observeSubjectProgress)) { overviews ->
                    overviews.toList()
                }
            }
        }

    private fun observeSubjectProgress(subject: Subject): Flow<SubjectProgressOverview> =
        progressRepository.observeProgressForSubject(subject.id).map { progressItems ->
            SubjectProgressOverview(
                subject = subject,
                completedSheets = progressItems.sumOf { it.completedSheets.coerceAtLeast(0) },
                totalSheets = progressItems.sumOf { it.totalSheets.coerceAtLeast(0) },
            ).normalized()
        }
}
