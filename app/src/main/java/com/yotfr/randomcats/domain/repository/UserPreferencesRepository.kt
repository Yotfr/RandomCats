package com.yotfr.randomcats.domain.repository

import com.yotfr.randomcats.domain.model.Theme
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    // update theme in dataStore
    suspend fun updateTheme(theme: Theme)

    // get current theme from dataStore
    fun getTheme(): Flow<Theme>
}
