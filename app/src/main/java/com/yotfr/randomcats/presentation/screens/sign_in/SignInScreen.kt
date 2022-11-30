package com.yotfr.randomcats.presentation.screens.sign_in

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
import com.yotfr.randomcats.domain.model.SignInModel
import com.yotfr.randomcats.presentation.screens.sign_in.event.SignInEvent
import com.yotfr.randomcats.presentation.screens.sign_in.event.SignInScreenEvent

@Composable
fun SignInScreen(
    onSignUp: () -> Unit,
    onSignIn: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {

    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        viewModel.event.collect{
            when(it) {
                SignInScreenEvent.NavigateHome -> {
                    onSignIn()
                }
            }
        }
    }

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
                    SignInEvent.SignInUser(
                        signInModel = SignInModel(
                            email = emailState,
                            password = passwordState
                        )
                    )
                )
            }) {

            }
            Button(onClick = {onSignUp()}) {

            }

        }
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        }
    }

}