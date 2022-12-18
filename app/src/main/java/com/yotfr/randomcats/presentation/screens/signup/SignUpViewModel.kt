package com.yotfr.randomcats.presentation.screens.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.model.Cause
import com.yotfr.randomcats.domain.model.Response
import com.yotfr.randomcats.domain.model.SignUpModel
import com.yotfr.randomcats.domain.use_case.users.UserUseCases
import com.yotfr.randomcats.presentation.screens.signup.event.SignUpEvent
import com.yotfr.randomcats.presentation.screens.signup.event.SignUpScreenEvent
import com.yotfr.randomcats.presentation.screens.signup.model.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {

    // state for composable
    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    // uiEvents
    private val _event = Channel<SignUpScreenEvent>()
    val event = _event.receiveAsFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.SignUpUser -> {
                _state.value.apply {
                    if (isValidatedBeforeCreateUser()) {
                        signUpUser(
                            signUpModel = SignUpModel(
                                email = emailText,
                                password = passwordText,
                                userName = usernameText
                            )
                        )
                    }
                }
            }
            is SignUpEvent.UpdateConfirmPasswordText -> {
                updateConfirmPassword(
                    confirmPasswordText = event.newText
                )
            }
            is SignUpEvent.UpdateEmailText -> {
                updateEmail(
                    emailText = event.newText
                )
            }
            is SignUpEvent.UpdatePasswordText -> {
                updatePassword(
                    passwordText = event.newText
                )
            }
            is SignUpEvent.UpdateUserNameText -> {
                updateUsername(
                    userNameText = event.newText
                )
            }
        }
    }

    private fun signUpUser(signUpModel: SignUpModel) {
        viewModelScope.launch {
            userUseCases.signUpUserUseCase(
                signUpModel = signUpModel
            ).collectLatest { result ->
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
                        sendEvent(SignUpScreenEvent.UserSuccessfullyCreated)
                    }
                    is Response.Exception -> {
                        when (result.cause) {
                            Cause.FirebaseUserAlreadyExistsException -> {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        isSuccess = false,
                                        emailText = "",
                                        passwordText = "",
                                        confirmPasswordText = "",
                                        isUserAlreadyExistsException = true
                                    )
                                }
                                sendEvent(SignUpScreenEvent.ShowUserAlreadyExistsSnackbar)
                            }
                            Cause.FirebaseEmailBadlyFormattedException -> {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        isSuccess = false,
                                        emailText = "",
                                        passwordText = "",
                                        confirmPasswordText = "",
                                        isUserAlreadyExistsException = false,
                                        isEmailInvalidError = true
                                    )
                                }
                            }
                            else -> Unit
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    // validate field on changedText
    private fun updateEmail(emailText: String) {
        viewModelScope.launch {
            if (emailText.isBlank()) {
                _state.update {
                    it.copy(
                        emailText = emailText,
                        isEmailInvalidError = false,
                        isEmailEmptyError = true,
                        isUserAlreadyExistsException = false
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
                        isUserAlreadyExistsException = false
                    )
                }
                return@launch
            }
            _state.update {
                it.copy(
                    emailText = emailText,
                    isEmailEmptyError = false,
                    isEmailInvalidError = false,
                    isUserAlreadyExistsException = false
                )
            }
            return@launch
        }
    }

    // validate field on changedText
    private fun updateUsername(userNameText: String) {
        viewModelScope.launch {
            if (userNameText.isBlank()) {
                _state.update {
                    it.copy(
                        usernameText = userNameText,
                        isUserNameEmptyError = true,
                        isUserAlreadyExistsException = false
                    )
                }
                return@launch
            }
            _state.update {
                it.copy(
                    usernameText = userNameText,
                    isUserNameEmptyError = false,
                    isUserAlreadyExistsException = false
                )
            }
            return@launch
        }
    }

    // validate field on changedText
    private fun updatePassword(passwordText: String) {
        viewModelScope.launch {
            if (passwordText.isBlank()) {
                _state.update {
                    it.copy(
                        passwordText = passwordText,
                        isPasswordEmptyError = true,
                        isUserAlreadyExistsException = false,
                        isPasswordTooShortError = false
                    )
                }
                return@launch
            }
            if (passwordText.length < 6 && passwordText != _state.value.confirmPasswordText) {
                _state.update {
                    it.copy(
                        passwordText = passwordText,
                        isPasswordEmptyError = false,
                        isUserAlreadyExistsException = false,
                        isPasswordTooShortError = true,
                        isPasswordsNotMatchError = true
                    )
                }
                return@launch
            }
            if (passwordText.length < 6) {
                _state.update {
                    it.copy(
                        passwordText = passwordText,
                        isPasswordEmptyError = false,
                        isUserAlreadyExistsException = false,
                        isPasswordTooShortError = true
                    )
                }
                return@launch
            }
            if (passwordText != _state.value.confirmPasswordText) {
                _state.update {
                    it.copy(
                        passwordText = passwordText,
                        isPasswordEmptyError = false,
                        isPasswordTooShortError = false,
                        isUserAlreadyExistsException = false,
                        isPasswordsNotMatchError = true
                    )
                }
                return@launch
            }
            _state.update {
                it.copy(
                    passwordText = passwordText,
                    isPasswordEmptyError = false,
                    isUserAlreadyExistsException = false,
                    isPasswordTooShortError = false,
                    isPasswordsNotMatchError = false
                )
            }
            return@launch
        }
    }

    // validate field on changedText
    private fun updateConfirmPassword(confirmPasswordText: String) {
        viewModelScope.launch {
            if (confirmPasswordText != _state.value.passwordText) {
                _state.update {
                    it.copy(
                        confirmPasswordText = confirmPasswordText,
                        isPasswordsNotMatchError = true,
                        isUserAlreadyExistsException = false
                    )
                }
                return@launch
            }
            _state.update {
                it.copy(
                    confirmPasswordText = confirmPasswordText,
                    isUserAlreadyExistsException = false,
                    isPasswordsNotMatchError = false
                )
            }
            return@launch
        }
    }

    // validating fields onetime before create user
    private fun isValidatedBeforeCreateUser(): Boolean {
        _state.value.apply {
            if (emailText.isBlank()) {
                _state.update {
                    it.copy(
                        isEmailInvalidError = false,
                        isEmailEmptyError = true,
                        isUserAlreadyExistsException = false
                    )
                }
                return false
            }
            if (isEmailInvalidError) {
                _state.update {
                    it.copy(
                        isEmailEmptyError = false,
                        isEmailInvalidError = true,
                        isUserAlreadyExistsException = false
                    )
                }
                return false
            }
            if (passwordText.isBlank()) {
                _state.update {
                    it.copy(
                        isPasswordEmptyError = true,
                        isUserAlreadyExistsException = false,
                        isPasswordTooShortError = false
                    )
                }
                return false
            }
            if (emailText.isBlank()) {
                _state.update {
                    it.copy(
                        isUserNameEmptyError = true,
                        isUserAlreadyExistsException = false
                    )
                }
                return false
            }
            if (passwordText.length < 6) {
                _state.update {
                    it.copy(
                        isPasswordEmptyError = false,
                        isUserAlreadyExistsException = false,
                        isPasswordTooShortError = true
                    )
                }
            }
            if (confirmPasswordText != _state.value.passwordText) {
                _state.update {
                    it.copy(
                        isPasswordsNotMatchError = true,
                        isUserAlreadyExistsException = false
                    )
                }
                return false
            }
            return true
        }
    }

    private fun sendEvent(uiEvent: SignUpScreenEvent) {
        viewModelScope.launch {
            _event.send(uiEvent)
        }
    }
}
