package com.yotfr.randomcats.presentation.screens.resetpassword

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.model.Cause
import com.yotfr.randomcats.domain.model.ResetPasswordModel
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.usecase.users.UserUseCases
import com.yotfr.randomcats.presentation.screens.resetpassword.event.ResetPasswordEvent
import com.yotfr.randomcats.presentation.screens.resetpassword.event.ResetPasswordScreenEvent
import com.yotfr.randomcats.presentation.screens.resetpassword.model.ResetPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ResetPasswordState())
    val state = _state.asStateFlow()

    private val _event = Channel<ResetPasswordScreenEvent>()
    val event = _event.receiveAsFlow()

    fun onEvent(event: ResetPasswordEvent) {
        when (event) {
            is ResetPasswordEvent.UpdateEmailText -> {
                updateEmail(
                    emailText = event.newText
                )
            }
            ResetPasswordEvent.ResetEmailClicked -> {
                resetEmail()
            }
        }
    }

    private fun resetEmail() {
        if (isValidatedBeforeSign()) {
            viewModelScope.launch {
                userUseCases.resetPasswordUseCase(
                    resetPasswordModel = ResetPasswordModel(
                        email = _state.value.emailText
                    )
                ).collectLatest { response ->
                    when (response) {
                        is Response.Loading -> {
                            _state.update {
                                it.copy(
                                    isLoading = true
                                )
                            }
                        }
                        is Response.Success -> {
                            _state.update {
                                it.copy(
                                    isLoading = false
                                )
                            }
                            sendToUi(ResetPasswordScreenEvent.ShowEmailLinkSentSnackbar)
                        }
                        is Response.Exception -> {
                            when (response.cause) {
                                is Cause.FirebaseEmailBadlyFormattedException -> {
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            isEmailInvalidError = true
                                        )
                                    }
                                }
                                is Cause.InvalidFirebaseCredentialsException -> {
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            isNoUserWithThisEmailError = true
                                        )
                                    }
                                    sendToUi(
                                        ResetPasswordScreenEvent.ShowNoUserCorrespondingEmailSnackbar
                                    )
                                }
                                is Cause.BadConnectionException -> {
                                    _state.update {
                                        it.copy(
                                            isLoading = false
                                        )
                                    }
                                    sendToUi(
                                        ResetPasswordScreenEvent.ShowNoInternetConnectionExceptionSnackbar
                                    )
                                }
                                is Cause.UnknownException -> {
                                    _state.update {
                                        it.copy(
                                            isLoading = false
                                        )
                                    }
                                }
                                else -> Unit
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateEmail(emailText: String) {
        viewModelScope.launch {
            if (emailText.isBlank()) {
                _state.update {
                    it.copy(
                        emailText = emailText,
                        isEmailInvalidError = false,
                        isEmailEmptyError = true,
                        isNoUserWithThisEmailError = false
                    )
                }
                return@launch
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                _state.update {
                    it.copy(
                        emailText = emailText,
                        isEmailEmptyError = false,
                        isEmailInvalidError = true,
                        isNoUserWithThisEmailError = false
                    )
                }
                return@launch
            }
            _state.update {
                it.copy(
                    emailText = emailText,
                    isEmailEmptyError = false,
                    isEmailInvalidError = false,
                    isNoUserWithThisEmailError = false
                )
            }
            return@launch
        }
    }

    private fun isValidatedBeforeSign(): Boolean {
        _state.value.apply {
            if (emailText.isEmpty() || emailText.isBlank()) {
                _state.update {
                    it.copy(
                        isEmailInvalidError = false,
                        isEmailEmptyError = true
                    )
                }
                return false
            }
            if (isEmailInvalidError) {
                _state.update {
                    it.copy(
                        isEmailEmptyError = false,
                        isEmailInvalidError = true
                    )
                }
                return false
            }
            return true
        }
    }

    private fun sendToUi(uiEvent: ResetPasswordScreenEvent) {
        viewModelScope.launch {
            _event.send(uiEvent)
        }
    }
}
