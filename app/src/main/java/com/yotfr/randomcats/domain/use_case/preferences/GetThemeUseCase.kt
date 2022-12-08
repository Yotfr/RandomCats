package com.yotfr.randomcats.domain.use_case.preferences

import com.yotfr.randomcats.domain.model.Theme
import com.yotfr.randomcats.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetThemeUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): Flow<Theme> = withContext(Dispatchers.IO) {
        userPreferencesRepository.getTheme()
    }
}
