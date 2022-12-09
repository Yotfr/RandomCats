package com.yotfr.randomcats.domain.repository

import com.yotfr.randomcats.domain.model.Language
import com.yotfr.randomcats.domain.model.Theme
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun updateTheme(theme:Theme)
    fun getTheme(): Flow<Theme>
    suspend fun updateLanguage(language:Language)
    fun getLanguage(): Flow<Language>
}