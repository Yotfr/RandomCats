package com.yotfr.randomcats.presentation.screens.sign_up.event

import com.yotfr.randomcats.domain.model.SignUpModel

sealed interface SignUpEvent{
    data class SignUpUser(val signUpModel: SignUpModel): SignUpEvent
}