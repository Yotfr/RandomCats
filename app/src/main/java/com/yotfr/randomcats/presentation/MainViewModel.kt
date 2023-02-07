package com.yotfr.randomcats.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.usecase.preferences.UserPreferencesUseCases
import com.yotfr.randomcats.domain.usecase.users.UserUseCases
import com.yotfr.randomcats.presentation.navigation.root.RootGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val userPreferencesUseCases: UserPreferencesUseCases
) : ViewModel() {

    private val _state = mutableStateOf(MainActivityState())
    val state: State<MainActivityState> = _state

    init {
        checkSettings()
    }

    private fun checkSettings() {
        _state.value = MainActivityState(
            isLoading = true
        )
        viewModelScope.launch {
            if (userUseCases.checkUserSignUseCase()) {
                _state.value = _state.value.copy(
                    startDestinationRoute = RootGraph.HOME,
                    isLoading = false
                )
            } else {
                _state.value = _state.value.copy(
                    startDestinationRoute = RootGraph.AUTH,
                    isLoading = false
                )
            }
        }
        viewModelScope.launch {
            userPreferencesUseCases.getThemeUseCase().collectLatest { theme ->
                _state.value = _state.value.copy(
                    theme = theme
                )
            }
        }
    }
}
