package com.yotfr.randomcats.presentation.screens.sign_in

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.model.MResult
import com.yotfr.randomcats.domain.model.SignInModel
import com.yotfr.randomcats.domain.use_case.users.UserUseCases
import com.yotfr.randomcats.presentation.screens.sign_in.event.SignInEvent
import com.yotfr.randomcats.presentation.screens.sign_in.event.SignInScreenEvent
import com.yotfr.randomcats.presentation.screens.sign_in.model.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userUseCases: UserUseCases
): ViewModel() {

    private val _state = mutableStateOf(SignInState())
    val state: State<SignInState> = _state

    private val _event = MutableSharedFlow<SignInScreenEvent>()
    val event = _event.asSharedFlow()

    fun onEvent(event: SignInEvent){
        when(event) {
            is SignInEvent.SignInUser -> {
                signInUser(
                    signInModel = event.signInModel
                )
            }
        }
    }

    private fun signInUser (signInModel: SignInModel) {
        viewModelScope.launch {
            userUseCases.signInUserUseCase(
                signInModel = signInModel
            ).collectLatest { result ->
                when(result) {
                    is MResult.Loading -> {
                        _state.value = SignInState(
                            isLoading = true
                        )
                    }
                    is MResult.Success -> {
                        _state.value = SignInState(
                            isSuccess = true
                        )
                        _event.emit(SignInScreenEvent.NavigateHome)
                    }
                    is MResult.Error -> {
                        _state.value = SignInState(
                            error = result.message ?:
                            "Unknown error occured")
                    }
                }
            }
        }
    }

}
