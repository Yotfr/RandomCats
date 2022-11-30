package com.yotfr.randomcats.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.use_case.users.UserUseCases
import com.yotfr.randomcats.presentation.screens.splash.event.SplashScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userUseCases: UserUseCases
):ViewModel() {

    private val _event = Channel<SplashScreenEvent>()
    val event = _event.receiveAsFlow()

    init {
        checkSign()
    }

    private fun checkSign(){
        viewModelScope.launch {
            if(userUseCases.checkUserSignUseCase()) {
                _event.send(SplashScreenEvent.NavigateHome)
            }else {
                _event.send(SplashScreenEvent.NavigateAuth)
            }
        }
    }

}