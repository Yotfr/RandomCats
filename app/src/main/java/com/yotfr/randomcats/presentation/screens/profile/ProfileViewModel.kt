package com.yotfr.randomcats.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.use_case.users.UserUseCases
import com.yotfr.randomcats.presentation.screens.profile.event.ProfileEvent
import com.yotfr.randomcats.presentation.screens.profile.event.ProfileScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userUseCases: UserUseCases
):ViewModel(){

    private val _event = Channel<ProfileScreenEvent>()
    val event = _event.receiveAsFlow()

    fun onEvent(event:ProfileEvent) {
        when(event) {
            ProfileEvent.SignOut -> {
                signOut()
            }
        }
    }

    private fun signOut(){
        viewModelScope.launch {
            userUseCases.signOutUseCase
            _event.send(ProfileScreenEvent.NavigateToAuth)
        }
    }
}