package com.raya.eightraya.di

import android.content.Context
import androidx.room.Room
import com.raya.eightraya.data.local.RayaDatabase
import com.raya.eightraya.data.local.dao.AiContentDao
import com.raya.eightraya.data.local.dao.DocumentDao
import com.raya.eightraya.data.local.dao.FlashcardDao
import com.raya.eightraya.data.local.dao.ProgressDao
import com.raya.eightraya.data.local.dao.SubjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideRayaDatabase(
        @ApplicationContext context: Context,
    ): RayaDatabase = Room.databaseBuilder(
        context,
        RayaDatabase::class.java,
        RayaDatabase.DATABASE_NAME,
    ).build()

    @Provides
    fun provideSubjectDao(database: RayaDatabase): SubjectDao = database.subjectDao()

    @Provides
    fun provideDocumentDao(database: RayaDatabase): DocumentDao = database.documentDao()

    @Provides
    fun provideFlashcardDao(database: RayaDatabase): FlashcardDao = database.flashcardDao()

    @Provides
    fun provideProgressDao(database: RayaDatabase): ProgressDao = database.progressDao()

    @Provides
    fun provideAiContentDao(database: RayaDatabase): AiContentDao = database.aiContentDao()
}
