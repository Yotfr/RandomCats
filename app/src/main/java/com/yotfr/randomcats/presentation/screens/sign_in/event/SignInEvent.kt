package com.yotfr.randomcats.presentation.screens.sign_in.event

sealed interface SignInEvent {
    object SignInUser:SignInEvent
    data class UpdateEmailText(val newText:String):SignInEvent
    data class UpdatePasswordText(val newText:String):SignInEvent
}