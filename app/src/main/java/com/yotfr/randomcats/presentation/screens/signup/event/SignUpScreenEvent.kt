package com.yotfr.randomcats.presentation.screens.signup.event


sealed interface SignUpScreenEvent {
    object ShowUserAlreadyExistsSnackbar: SignUpScreenEvent
    object UserSuccessfullyCreated: SignUpScreenEvent
}