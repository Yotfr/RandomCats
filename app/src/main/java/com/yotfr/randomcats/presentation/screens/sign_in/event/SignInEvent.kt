package com.yotfr.randomcats.presentation.screens.sign_in.event

import com.yotfr.randomcats.domain.model.SignInModel

sealed interface SignInEvent {
    data class SignInUser(val signInModel: SignInModel):SignInEvent
}