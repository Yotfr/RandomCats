package com.yotfr.randomcats.presentation.screens.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.yotfr.randomcats.domain.model.SignUpModel
import com.yotfr.randomcats.presentation.screens.sign_up.event.SignUpEvent

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val state = viewModel.state.value

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            var emailState by remember {
                mutableStateOf("")
            }
            var passwordState by remember {
                mutableStateOf("")
            }

            TextField(
                value = emailState,
                onValueChange = { newValue ->
                    emailState = newValue
                }
            )
            TextField(
                value = passwordState,
                onValueChange = { newValue ->
                    passwordState = newValue
                }
            )
            Button(onClick = {
                viewModel.onEvent(
                    SignUpEvent.SignUpUser(
                        signUpModel = SignUpModel(
                            email = emailState,
                            password = passwordState
                        )
                    )
                )
            }) {

            }
        }
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize().align(Alignment.Center))
        }
    }

}