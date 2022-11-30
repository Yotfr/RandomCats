package com.yotfr.randomcats.presentation.screens.sign_up

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.randomcats.domain.model.MResult
import com.yotfr.randomcats.domain.model.SignUpModel
import com.yotfr.randomcats.domain.use_case.users.UserUseCases
import com.yotfr.randomcats.presentation.screens.sign_up.event.SignUpEvent
import com.yotfr.randomcats.presentation.screens.sign_up.model.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userUseCases: UserUseCases
):ViewModel() {

    private val _state = mutableStateOf(SignUpState())
    val state: State<SignUpState> = _state

    fun onEvent(event:SignUpEvent){
        when(event) {
            is SignUpEvent.SignUpUser -> {
                signUpUser(
                    signUpModel = event.signUpModel
                )
            }
        }
    }

    private fun signUpUser(signUpModel: SignUpModel) {
        viewModelScope.launch {
            userUseCases.signUpUserUseCase(
                signUpModel = signUpModel
            ).collectLatest { result ->
                Log.d("TEST","$signUpModel")

                when(result) {
                    is MResult.Loading -> {
                        _state.value = SignUpState(
                            isLoading = true
                        )
                    }
                    is MResult.Success -> {
                        _state.value = SignUpState(
                            isSuccess = true
                        )
                    }
                    is MResult.Error -> {
                        _state.value = SignUpState(
                            error = result.message ?:
                            "Unknown error occured")
                        Log.d("TEST","${result.message}")
                    }
                }
            }
        }
    }

}