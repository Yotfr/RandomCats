package com.yotfr.randomcats.presentation.screens.resetpassword.event

sealed interface ResetPasswordEvent {
    data class UpdateEmailText(val newText: String) : ResetPasswordEvent
    object SendResetEmail: ResetPasswordEvent
}
