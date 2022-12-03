package com.yotfr.randomcats.presentation.screens.sign_up.event


sealed interface SignUpScreenEvent {
    object NavigateHome: SignUpScreenEvent
    object ShowUserAlreadyExistsError: SignUpScreenEvent
}