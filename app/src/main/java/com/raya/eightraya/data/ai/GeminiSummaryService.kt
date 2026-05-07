package com.raya.eightraya.data.ai

import com.google.genai.Client
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class GeminiSummaryService @Inject constructor(
    private val client: Client?,
) {
    suspend fun generateConciseSummary(sourceText: String): String = withContext(Dispatchers.IO) {
        val activeClient = client
            ?: throw IllegalStateException(
                "Add GEMINI_API_KEY to local.properties. Create a key at https://aistudio.google.com/app/apikey.",
            )

        val boundedText = sourceText.take(MAX_INPUT_CHARS)
        val prompt = buildString {
            append(
                "You are a study assistant. Read the following document excerpt and write a concise summary ",
            )
            append(
                "for a university student. Use short paragraphs and bullet points where they improve clarity. ",
            )
            append("Stay under 400 words. Do not invent facts that are not supported by the text.\n\n---\n\n")
            append(boundedText)
        }

        val response = activeClient.models.generateContent(MODEL_NAME, prompt, null)
        val summaryText = response.text()
        if (summaryText.isNullOrBlank()) {
            throw IllegalStateException("The model returned an empty summary.")
        }
        summaryText.trim()
    }

    companion object {
        private const val MODEL_NAME = "gemini-1.5-flash"
        private const val MAX_INPUT_CHARS = 80_000
    }
}
