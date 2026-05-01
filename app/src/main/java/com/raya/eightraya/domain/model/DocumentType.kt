package com.raya.eightraya.domain.model

sealed class DocumentType {
    data object Pdf : DocumentType()
    data object Markdown : DocumentType()
}
