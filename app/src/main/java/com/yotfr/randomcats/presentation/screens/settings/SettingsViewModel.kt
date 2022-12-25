package com.yotfr.randomcats.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.use_case.users.UserUseCases
import com.yotfr.randomcats.presentation.screens.settings.event.SettingsEvent
import com.yotfr.randomcats.presentation.screens.settings.event.SettingsScreenEvent
import com.yotfr.randomcats.presentation.screens.settings.model.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<SettingsScreenEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getProfileInfo()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.SignOutPressed -> {
                signOut()
            }
            SettingsEvent.ThemePressed -> {
                navigateToThemeScreen()
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            userUseCases.signOutUseCase().collectLatest { result ->
                when (result) {
                    is Response.Success -> {
                        sendUiEvent(SettingsScreenEvent.NavigateToAuth)
                    }
                    is Response.Exception -> {
                        // TODO
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun getProfileInfo() {
        viewModelScope.launch {
            userUseCases.getUserProfileInfoUseCase().collectLatest { response ->
                when (response) {
                    is Response.Success -> {
                        _state.update {
                            it.copy(
                                email = response.data.email,
                                userName = response.data.userName
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun navigateToThemeScreen() {
        sendUiEvent(SettingsScreenEvent.NavigateToThemeScreen)
    }

    private fun sendUiEvent(uiEvent: SettingsScreenEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}
