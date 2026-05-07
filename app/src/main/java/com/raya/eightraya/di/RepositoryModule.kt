package com.raya.eightraya.di

import com.raya.eightraya.data.repository.RoomDocumentRepository
import com.raya.eightraya.data.repository.RoomProgressRepository
import com.raya.eightraya.data.repository.RoomSubjectRepository
import com.raya.eightraya.domain.repository.DocumentRepository
import com.raya.eightraya.domain.repository.ProgressRepository
import com.raya.eightraya.domain.repository.SubjectRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSubjectRepository(
        repository: RoomSubjectRepository,
    ): SubjectRepository

    @Binds
    @Singleton
    abstract fun bindProgressRepository(
        repository: RoomProgressRepository,
    ): ProgressRepository

    @Binds
    @Singleton
    abstract fun bindDocumentRepository(
        repository: RoomDocumentRepository,
    ): DocumentRepository
}
