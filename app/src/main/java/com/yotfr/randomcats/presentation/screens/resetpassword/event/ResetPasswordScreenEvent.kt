package com.yotfr.randomcats.presentation.screens.resetpassword.event

sealed interface ResetPasswordScreenEvent {
    object ShowNoUserCorrespondingEmailSnackbar : ResetPasswordScreenEvent
    object ShowEmailLinkSentSnackbar : ResetPasswordScreenEvent
}
