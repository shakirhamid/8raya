package com.raya.eightraya.domain.repository

import com.raya.eightraya.domain.model.StudyContentResult

interface AiProcessingRepository {
    suspend fun processDocumentText(text: String): Result<StudyContentResult>
}
