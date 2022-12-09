package com.yotfr.randomcats.presentation.screens.settingstheme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.model.Theme
import com.yotfr.randomcats.domain.use_case.preferences.UserPreferencesUseCases
import com.yotfr.randomcats.presentation.screens.settingstheme.event.SettingsThemeEvent
import com.yotfr.randomcats.presentation.screens.settingstheme.model.SettingsThemeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsThemeViewModel @Inject constructor(
    private val userPreferencesUseCases: UserPreferencesUseCases
):ViewModel() {

    private val _state = MutableStateFlow(SettingsThemeState())
    val state = _state.asStateFlow()

    init {
        getTheme()
    }

    fun onEvent(event:SettingsThemeEvent){
        when(event) {
            is SettingsThemeEvent.ThemeChanged -> {
                updateTheme(
                    theme = event.theme
                )
            }
        }
    }

    private fun updateTheme(theme:Theme){
        viewModelScope.launch {
            userPreferencesUseCases.updateThemeUseCase(
                theme = theme
            )
        }
    }

    private fun getTheme(){
        viewModelScope.launch {
            userPreferencesUseCases.getThemeUseCase().collectLatest { theme ->
                _state.update {
                    it.copy(
                        currentTheme = theme
                    )
                }
            }
        }
    }
}