package com.raya.eightraya.data.local

import androidx.room.TypeConverter
import com.raya.eightraya.domain.model.DocumentType
import com.raya.eightraya.domain.model.ProcessingStatus
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class RayaTypeConverters {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    private val stringListSerializer = ListSerializer(String.serializer())

    @TypeConverter
    fun instantToMillis(value: Instant?): Long? = value?.toEpochMilli()

    @TypeConverter
    fun millisToInstant(value: Long?): Instant? = value?.let(Instant::ofEpochMilli)

    @TypeConverter
    fun documentTypeToString(value: DocumentType?): String? = when (value) {
        DocumentType.Pdf -> "PDF"
        DocumentType.Markdown -> "MARKDOWN"
        null -> null
    }

    @TypeConverter
    fun stringToDocumentType(value: String?): DocumentType? = when (value) {
        "PDF" -> DocumentType.Pdf
        "MARKDOWN" -> DocumentType.Markdown
        null -> null
        else -> error("Unknown document type: $value")
    }

    @TypeConverter
    fun processingStatusToString(value: ProcessingStatus?): String? = when (value) {
        ProcessingStatus.Pending -> "PENDING"
        ProcessingStatus.Processing -> "PROCESSING"
        ProcessingStatus.Completed -> "COMPLETED"
        is ProcessingStatus.Failed -> "FAILED|${encode(value.reason)}"
        null -> null
    }

    @TypeConverter
    fun stringToProcessingStatus(value: String?): ProcessingStatus? = when {
        value == null -> null
        value == "PENDING" -> ProcessingStatus.Pending
        value == "PROCESSING" -> ProcessingStatus.Processing
        value == "COMPLETED" -> ProcessingStatus.Completed
        value.startsWith("FAILED|") -> ProcessingStatus.Failed(decode(value.substringAfter("FAILED|")))
        else -> error("Unknown processing status: $value")
    }

    @TypeConverter
    fun stringListToJson(value: List<String>?): String? =
        value?.let { json.encodeToString(stringListSerializer, it) }

    @TypeConverter
    fun jsonToStringList(value: String?): List<String>? =
        value?.let { json.decodeFromString(stringListSerializer, it) }

    private fun encode(value: String): String =
        URLEncoder.encode(value, StandardCharsets.UTF_8.name())

    private fun decode(value: String): String =
        URLDecoder.decode(value, StandardCharsets.UTF_8.name())
}
