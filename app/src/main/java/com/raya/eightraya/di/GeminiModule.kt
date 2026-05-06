package com.raya.eightraya.di

import com.google.genai.Client
import com.raya.eightraya.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeminiModule {
    @Provides
    @Singleton
    fun provideGeminiClient(): Client? {
        val apiKey = BuildConfig.GEMINI_API_KEY
        return if (apiKey.isBlank()) {
            null
        } else {
            Client.builder().apiKey(apiKey).build()
        }
    }
}
