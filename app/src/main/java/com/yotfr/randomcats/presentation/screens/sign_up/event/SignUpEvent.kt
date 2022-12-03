package com.yotfr.randomcats.presentation.screens.sign_up.event

sealed interface SignUpEvent{
    object SignUpUser: SignUpEvent
    data class UpdateEmailText(val newText:String):SignUpEvent
    data class UpdatePasswordText(val newText:String):SignUpEvent
    data class UpdateConfirmPasswordText(val newText:String):SignUpEvent
}