package com.yotfr.randomcats.presentation.screens.signin

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.model.Cause
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.model.SignInModel
import com.yotfr.randomcats.domain.use_case.users.UserUseCases
import com.yotfr.randomcats.presentation.screens.signin.event.SignInEvent
import com.yotfr.randomcats.presentation.screens.signin.event.SignInScreenEvent
import com.yotfr.randomcats.presentation.screens.signin.model.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {

    // state for composable
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    // uiEvents
    private val _event = Channel<SignInScreenEvent>()
    val event = _event.receiveAsFlow()

    fun onEvent(event: SignInEvent) {
        when (event) {
            SignInEvent.SignInUser -> {
                _state.value.apply {
                    if (isValidatedBeforeSign()) {
                        signInUser(
                            signInModel = SignInModel(
                                email = emailText,
                                password = passwordText
                            )
                        )
                    }
                }
            }
            is SignInEvent.UpdateEmailText -> {
                updateEmail(event.newText)
            }
            is SignInEvent.UpdatePasswordText -> {
                updatePassword(event.newText)
            }
        }
    }

    private fun signInUser(signInModel: SignInModel) {
        viewModelScope.launch {
            userUseCases.signInUserUseCase(
                signInModel = signInModel
            ).collectLatest { result ->
                Log.d("TEST","exce -> $result")
                when (result) {
                    is Response.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                isSuccess = false
                            )
                        }
                    }
                    is Response.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = true
                            )
                        }
                        sendEvent(SignInScreenEvent.NavigateHome)
                    }
                    is Response.Exception -> {
                        when (result.cause) {
                            Cause.InvalidFirebaseCredentialsException -> {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        isSuccess = false,
                                        emailText = "",
                                        passwordText = "",
                                        isInvalidCredentialsError = true
                                    )
                                }
                                sendEvent(SignInScreenEvent.ShowInvalidCredentialsError)
                            }
                            is Cause.UnknownException -> {
                                Log.d("TEST","exce -> ${result.cause.message}")
                            }
                            else -> Unit
                        }
                    }
                    else -> Unit
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
                        isInvalidCredentialsError = false
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
                        isInvalidCredentialsError = false
                    )
                }
                return@launch
            }
            _state.update {
                it.copy(
                    emailText = emailText,
                    isEmailEmptyError = false,
                    isEmailInvalidError = false,
                    isInvalidCredentialsError = false
                )
            }
            return@launch
        }
    }

    private fun updatePassword(passwordText: String) {
        viewModelScope.launch {
            if (passwordText.isBlank()) {
                _state.update {
                    it.copy(
                        passwordText = passwordText,
                        isPasswordEmptyError = true,
                        isInvalidCredentialsError = false
                    )
                }
                return@launch
            }
            _state.update {
                it.copy(
                    passwordText = passwordText,
                    isPasswordEmptyError = false,
                    isInvalidCredentialsError = false
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
                        isEmailEmptyError = true,
                        isInvalidCredentialsError = false
                    )
                }
                return false
            }
            if (isEmailInvalidError) {
                _state.update {
                    it.copy(
                        isEmailEmptyError = false,
                        isEmailInvalidError = true,
                        isInvalidCredentialsError = false
                    )
                }
                return false
            }
            if (passwordText.isEmpty() || passwordText.isBlank()) {
                _state.update {
                    it.copy(
                        isPasswordEmptyError = true,
                        isInvalidCredentialsError = false
                    )
                }
                return false
            }
            return true
        }
    }

    private fun sendEvent(uiEvent: SignInScreenEvent) {
        viewModelScope.launch { _event.send(uiEvent)
        }
    }
}
