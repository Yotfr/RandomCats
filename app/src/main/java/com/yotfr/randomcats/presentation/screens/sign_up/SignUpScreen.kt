package com.yotfr.randomcats.presentation.screens.sign_up

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yotfr.randomcats.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val state = viewModel.state.value

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            Icons.Outlined.ArrowBack,
                            contentDescription = stringResource(id = R.string.go_back)
                        )
                    }
                },
                title = {}
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = 32.dp,
                    start = 32.dp,
                    end = 32.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            )
            SignUpTitle(
                modifier = Modifier.fillMaxWidth(),
                titleText = stringResource(id = R.string.create_account),
                subTitleText = stringResource(id = R.string.fill_input_below)
            )
            Spacer (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            )
            SignUpInputFields(
                modifier = Modifier.fillMaxWidth(),
                emailLabelText = stringResource(id = R.string.email),
                passwordLabelText = stringResource(id = R.string.password),
                confirmPasswordLabelText = stringResource(id = R.string.confirm_password),
                showPasswordText = stringResource(id = R.string.show_password),
                hidePasswordText = stringResource(id = R.string.hide_password)
            )
            Spacer (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            )
            SignUpButton (
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 52.dp),
                signUpButtonText = stringResource(id = R.string.sign_up),
                onSignUpClicked = {
                    //TODO
                }
            )
            Spacer (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            ToSignInText (
                modifier = Modifier.fillMaxWidth(),
                toSignInText = stringResource(id = R.string.already_have_acc),
                toSignInButtonText = stringResource(id = R.string.sign_in),
                onSignUpClicked = {
                    //TODO
                }
            )
        }
    }
}


@Composable
fun SignUpTitle(
    modifier: Modifier,
    titleText: String,
    subTitleText: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = titleText,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(6.dp)
        )
        Text(
            modifier = Modifier.alpha(0.7f),
            text = subTitleText,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpInputFields(
    modifier: Modifier,
    emailLabelText: String,
    passwordLabelText: String,
    confirmPasswordLabelText: String,
    showPasswordText: String,
    hidePasswordText: String
) {

    var loginText by rememberSaveable {
        mutableStateOf("")
    }
    var passwordText by rememberSaveable {
        mutableStateOf("")
    }
    var confirmPasswordText by rememberSaveable {
        mutableStateOf("")
    }

    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var confirmPasswordHidden by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = loginText,
            onValueChange = { loginText = it },
            label = { Text(text = emailLabelText) },
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = emailLabelText) }
        )
        OutlinedTextField(
            value = passwordText,
            onValueChange = { passwordText = it },
            label = { Text(text = passwordLabelText) },
            visualTransformation =
            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = passwordLabelText) },
            trailingIcon = {
                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                    val visibilityIcon =
                        if (passwordHidden) Icons.Outlined.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordHidden) showPasswordText else hidePasswordText
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                }
            }
        )
        OutlinedTextField(
            value = confirmPasswordText,
            onValueChange = { confirmPasswordText = it },
            label = { Text(text = confirmPasswordLabelText) },
            visualTransformation =
            if (confirmPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = confirmPasswordLabelText) },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordHidden = !confirmPasswordHidden }) {
                    val visibilityIcon =
                        if (confirmPasswordHidden) Icons.Outlined.Visibility else Icons.Filled.VisibilityOff
                    val description =
                        if (confirmPasswordHidden) showPasswordText else hidePasswordText
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                }
            }
        )
    }
}

@Composable
fun SignUpButton(
    modifier: Modifier,
    signUpButtonText: String,
    onSignUpClicked: () -> Unit,
) {
        Button(
            modifier = modifier,
            onClick = {
                onSignUpClicked()
            }
        ) {
            Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = signUpButtonText.uppercase(),
                style = MaterialTheme.typography.titleMedium
            )
        }
}


@Composable
fun ToSignInText(
    modifier: Modifier,
    toSignInText: String,
    toSignInButtonText: String,
    onSignUpClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.alpha(0.7f),
            color = MaterialTheme.colorScheme.secondary,
            text = toSignInText,
            style = MaterialTheme.typography.titleMedium
        )
        TextButton(
            onClick = { onSignUpClicked() }
        ) {
            Text(
                text = toSignInButtonText,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}