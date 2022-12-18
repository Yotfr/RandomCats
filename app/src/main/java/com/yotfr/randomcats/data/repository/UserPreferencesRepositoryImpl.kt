package com.yotfr.randomcats.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yotfr.randomcats.domain.model.Theme
import com.yotfr.randomcats.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    override suspend fun updateTheme(theme: Theme) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = theme.themeString
        }
    }

    override fun getTheme(): Flow<Theme> {
        val theme = dataStore.data
            .map { preferences ->
                val prefThemeKey = preferences[PreferencesKeys.THEME]
                Theme.values().firstOrNull { it.themeString == prefThemeKey } ?: Theme.SYSTEM_DEFAULT
            }
        return theme
    }

    private object PreferencesKeys {
        val THEME = stringPreferencesKey("THEME")
    }
}
