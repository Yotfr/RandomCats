package com.yotfr.randomcats.presentation.screens.sign_up.event


sealed interface SignUpScreenEvent {
    object ShowUserAlreadyExistsSnackbar: SignUpScreenEvent
    object UserSuccessfullyCreated: SignUpScreenEvent
}