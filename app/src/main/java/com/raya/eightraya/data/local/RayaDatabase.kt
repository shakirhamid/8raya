package com.raya.eightraya.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.raya.eightraya.data.local.dao.AiContentDao
import com.raya.eightraya.data.local.dao.DocumentDao
import com.raya.eightraya.data.local.dao.FlashcardDao
import com.raya.eightraya.data.local.dao.ProgressDao
import com.raya.eightraya.data.local.dao.SubjectDao
import com.raya.eightraya.data.local.entity.AiSummaryEntity
import com.raya.eightraya.data.local.entity.DocumentEntity
import com.raya.eightraya.data.local.entity.FlashcardEntity
import com.raya.eightraya.data.local.entity.McqChoiceEntity
import com.raya.eightraya.data.local.entity.McqEntity
import com.raya.eightraya.data.local.entity.SheetProgressEntity
import com.raya.eightraya.data.local.entity.SubjectEntity

@Database(
    entities = [
        SubjectEntity::class,
        DocumentEntity::class,
        FlashcardEntity::class,
        SheetProgressEntity::class,
        AiSummaryEntity::class,
        McqEntity::class,
        McqChoiceEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(RayaTypeConverters::class)
abstract class RayaDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun documentDao(): DocumentDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun progressDao(): ProgressDao
    abstract fun aiContentDao(): AiContentDao

    companion object {
        const val DATABASE_NAME = "raya.db"
    }
}
