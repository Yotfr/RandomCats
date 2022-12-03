package com.yotfr.randomcats.presentation.screens.sign_in

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.screens.sign_in.event.SignInEvent
import com.yotfr.randomcats.presentation.screens.sign_in.event.SignInScreenEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    toSignUp: () -> Unit,
    signedIn: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    //collect uiEvents
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                SignInScreenEvent.NavigateHome -> {
                    signedIn()
                }
                SignInScreenEvent.ShowInvalidCredentialsError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.resources.getString(R.string.invalid_credentials)
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LoginImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                imageResource = painterResource(id = R.drawable.cat_login_screen)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            )
            LoginTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                titleText = stringResource(id = R.string.login),
                subTitleText = stringResource(id = R.string.fill_input_below)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            )
            SignInInputFields(
                modifier = Modifier.fillMaxWidth(),
                emailLabelText = stringResource(id = R.string.email),
                passwordLabelText = stringResource(id = R.string.password),
                showPasswordText = stringResource(id = R.string.show_password),
                hidePasswordText = stringResource(id = R.string.hide_password),
                emailText = state.emailText,
                passwordText = state.passwordText,
                onEmailTextChanged = { newText ->
                    viewModel.onEvent(
                        SignInEvent.UpdateEmailText(
                            newText = newText
                        )
                    )
                },
                onPasswordTextChanged = { newText ->
                    viewModel.onEvent(
                        SignInEvent.UpdatePasswordText(
                            newText = newText
                        )
                    )
                },
                isEmailInvalidError = state.isEmailInvalidError,
                isEmailEmptyError = state.isEmailEmptyError,
                isPasswordEmptyError = state.isPasswordEmptyError,
                isInvalidCredentialsError = state.isInvalidCredentialsError,
                emptyEmailFieldErrorMessage = stringResource(id = R.string.enter_email_adress),
                emptyPasswordFieldErrorMessage = stringResource(id = R.string.enter_password),
                invalidEmailErrorMessage = stringResource(id = R.string.email_adress_not_valid)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            )
            LoginButton(
                modifier = Modifier.wrapContentSize(),
                forgotPasswordText = stringResource(id = R.string.forgot_password),
                loginButtonText = stringResource(id = R.string.login),
                onLoginClicked = {
                    viewModel.onEvent(SignInEvent.SignInUser)
                },
                onForgotPasswordClicked = {
                    //TODO
                },
                isLoading = state.isLoading
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            ToSignUpText(
                modifier = Modifier.fillMaxWidth(),
                toSignUpText = stringResource(id = R.string.do_not_have_acc),
                toSignUpButtonText = stringResource(id = R.string.sign_up),
                onSignUpClicked = { toSignUp() }
            )
        }
    }


}

@Composable
fun LoginImage(
    modifier: Modifier,
    imageResource: Painter
) {
    Image(
        modifier = modifier,
        painter = imageResource,
        contentDescription = "",
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun LoginTitle(
    modifier: Modifier,
    titleText: String,
    subTitleText: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.Start
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
fun SignInInputFields(
    modifier: Modifier,
    emailLabelText: String,
    passwordLabelText: String,
    showPasswordText: String,
    hidePasswordText: String,
    emailText: String,
    passwordText: String,
    onEmailTextChanged: (text: String) -> Unit,
    onPasswordTextChanged: (text: String) -> Unit,
    isEmailEmptyError: Boolean,
    isPasswordEmptyError: Boolean,
    isEmailInvalidError: Boolean,
    isInvalidCredentialsError: Boolean,
    emptyEmailFieldErrorMessage: String,
    emptyPasswordFieldErrorMessage: String,
    invalidEmailErrorMessage: String
) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = emailText,
            onValueChange = { onEmailTextChanged(it) },
            label = { Text(text = emailLabelText) },
            isError = isEmailEmptyError || isEmailInvalidError || isInvalidCredentialsError,
            singleLine = true,
            supportingText = {
                if (isEmailEmptyError) {
                    Text(text = emptyEmailFieldErrorMessage)
                }
                if (isEmailInvalidError) {
                    Text(text = invalidEmailErrorMessage)
                }
            },
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = emailLabelText) }
        )
        OutlinedTextField(
            value = passwordText,
            onValueChange = { onPasswordTextChanged(it) },
            label = { Text(text = passwordLabelText) },
            isError = isPasswordEmptyError || isInvalidCredentialsError,
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
            },
            supportingText = {
                if (isPasswordEmptyError) {
                    Text(text = emptyPasswordFieldErrorMessage)
                }
            }
        )
    }
}

@Composable
fun LoginButton(
    modifier: Modifier,
    forgotPasswordText: String,
    loginButtonText: String,
    onLoginClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 52.dp),
            onClick = {
                onLoginClicked()
            }
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = loginButtonText.uppercase(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }
        TextButton(
            onClick = {
                onForgotPasswordClicked()
            }
        ) {
            Text(
                text = forgotPasswordText,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


@Composable
fun ToSignUpText(
    modifier: Modifier,
    toSignUpText: String,
    toSignUpButtonText: String,
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
            text = toSignUpText,
            style = MaterialTheme.typography.titleMedium
        )
        TextButton(
            onClick = { onSignUpClicked() }
        ) {
            Text(
                text = toSignUpButtonText,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}