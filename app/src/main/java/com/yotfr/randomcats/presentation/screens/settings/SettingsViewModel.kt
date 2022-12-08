package com.yotfr.randomcats.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.use_case.users.UserUseCases
import com.yotfr.randomcats.presentation.screens.settings.event.SettingsEvent
import com.yotfr.randomcats.presentation.screens.settings.event.SettingsScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userUseCases: UserUseCases
):ViewModel(){

    private val _event = Channel<SettingsScreenEvent>()
    val event = _event.receiveAsFlow()

    fun onEvent(event:SettingsEvent) {
        when(event) {
            SettingsEvent.SignOut -> {
                signOut()
            }
        }
    }

    private fun signOut(){
        viewModelScope.launch {
            userUseCases.signOutUseCase
            _event.send(SettingsScreenEvent.NavigateToAuth)
        }
    }
}