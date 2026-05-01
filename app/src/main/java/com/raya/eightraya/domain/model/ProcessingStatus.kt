package com.raya.eightraya.domain.model

sealed class ProcessingStatus {
    data object Pending : ProcessingStatus()
    data object Processing : ProcessingStatus()
    data object Completed : ProcessingStatus()
    data class Failed(val reason: String) : ProcessingStatus()
}
