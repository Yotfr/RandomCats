package com.yotfr.randomcats.domain.use_case.preferences

import com.yotfr.randomcats.domain.model.Theme
import com.yotfr.randomcats.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateThemeUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {

    suspend operator fun invoke(theme:Theme) {
        withContext(Dispatchers.IO) {
           userPreferencesRepository.updateTheme(
               theme = theme
           )
        }
    }
}