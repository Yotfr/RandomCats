package com.yotfr.randomcats.presentation.screens.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.screens.signin.event.SignInEvent
import com.yotfr.randomcats.presentation.screens.signin.event.SignInScreenEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    toSignUp: () -> Unit,
    toMain: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    // collect uiEvents
    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.event.collect {
                when (it) {
                    SignInScreenEvent.NavigateHome -> {
                        toMain()
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
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(
                    top = dimensionResource(id = R.dimen.small_padding),
                    start = dimensionResource(id = R.dimen.large_padding),
                    end = dimensionResource(id = R.dimen.large_padding)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LoginImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                imageResource = painterResource(id = R.drawable.cat_log)
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
                modifier = Modifier
                    .fillMaxWidth(),
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
                    // TODO
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
        contentScale = ContentScale.Crop
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
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(6.dp)
        )
        Text(
            modifier = Modifier.alpha(0.5f),
            text = subTitleText,
            color = MaterialTheme.colorScheme.onBackground,
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
        verticalArrangement = Arrangement.SpaceAround
    ) {
        var emailTextFocused by remember { mutableStateOf(false) }
        var passwordTextFocused by remember { mutableStateOf(false) }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    emailTextFocused = it.isFocused
                },
            colors = TextFieldDefaults.textFieldColors(
                textColor = if (isEmailEmptyError || isEmailInvalidError || isInvalidCredentialsError) {
                    MaterialTheme.colorScheme.error
                } else MaterialTheme.colorScheme.onBackground,
                containerColor = if (isEmailEmptyError || isEmailInvalidError || isInvalidCredentialsError) {
                    MaterialTheme.colorScheme.error.copy(
                        alpha = 0.12f
                    )
                } else if (emailTextFocused) {
                    MaterialTheme.colorScheme.primaryContainer.copy(
                        alpha = 0.3f
                    )
                } else Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.7f
                ),
                focusedLeadingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.7f
                ),
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.7f
                ),
                focusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.7f
                ),
                errorLeadingIconColor = MaterialTheme.colorScheme.error,
                errorIndicatorColor = Color.Transparent
            ),
            value = emailText,
            onValueChange = { onEmailTextChanged(it) },
            label = {
                Text(
                    text = emailLabelText,
                    style = if (emailTextFocused) MaterialTheme.typography.bodyMedium
                    else MaterialTheme.typography.bodyLarge
                )
            },
            isError = isEmailEmptyError || isEmailInvalidError || isInvalidCredentialsError,
            singleLine = true,
            supportingText = {
                Row(
                    modifier = Modifier.padding(
                        start = 52.dp
                    )
                ) {
                    if (isEmailEmptyError) {
                        Text(text = emptyEmailFieldErrorMessage)
                    }
                    if (isEmailInvalidError) {
                        Text(text = invalidEmailErrorMessage)
                    }
                }
            },
            leadingIcon = {
                Row(
                    modifier = Modifier.padding(
                        start = 32.dp,
                        end = 8.dp
                    )
                ) {
                    Icon(Icons.Outlined.Email, contentDescription = emailLabelText)
                }
            },
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    passwordTextFocused = it.isFocused
                },
            colors = TextFieldDefaults.textFieldColors(
                textColor = if (isPasswordEmptyError || isInvalidCredentialsError) {
                    MaterialTheme.colorScheme.error
                } else MaterialTheme.colorScheme.onBackground,
                containerColor = if (isPasswordEmptyError || isInvalidCredentialsError) {
                    MaterialTheme.colorScheme.error.copy(
                        alpha = 0.12f
                    )
                } else if (passwordTextFocused) {
                    MaterialTheme.colorScheme.primaryContainer.copy(
                        alpha = 0.3f
                    )
                } else Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.7f
                ),
                focusedTrailingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.7f
                ),
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.7f
                ),
                focusedLeadingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.7f
                ),
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.7f
                ),
                focusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.7f
                ),
                errorLeadingIconColor = MaterialTheme.colorScheme.error,
                errorIndicatorColor = Color.Transparent
            ),
            value = passwordText,
            onValueChange = { onPasswordTextChanged(it) },
            label = {
                Text(
                    text = passwordLabelText,
                    style = if (passwordTextFocused) MaterialTheme.typography.bodyMedium
                    else MaterialTheme.typography.bodyLarge
                )
            },
            isError = isPasswordEmptyError || isInvalidCredentialsError,
            visualTransformation =
            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            leadingIcon = {
                Row(
                    modifier = Modifier.padding(
                        start = 32.dp,
                        end = 8.dp
                    )
                ) {
                    Icon(Icons.Outlined.Lock, contentDescription = passwordLabelText)
                }
            },
            trailingIcon = {
                if (passwordText.isNotBlank()) {
                    Row(
                        modifier = Modifier.padding(
                            end = 32.dp,
                            start = 8.dp
                        )
                    ) {
                        IconButton(onClick = { passwordHidden = !passwordHidden }) {
                            val visibilityIcon =
                                if (passwordHidden) Icons.Outlined.Visibility else Icons.Filled.VisibilityOff
                            val description =
                                if (passwordHidden) showPasswordText else hidePasswordText
                            Icon(imageVector = visibilityIcon, contentDescription = description)
                        }
                    }
                }
            },
            supportingText = {
                Row(
                    modifier = Modifier.padding(
                        start = 32.dp
                    )
                ) {
                    if (isPasswordEmptyError) {
                        Text(text = emptyPasswordFieldErrorMessage)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp)
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
                .padding(
                    horizontal = 64.dp
                ),
            onClick = {
                onLoginClicked()
            }
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .align(Alignment.CenterVertically)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = loginButtonText.uppercase(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
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
            modifier = Modifier.alpha(0.5f),
            color = MaterialTheme.colorScheme.onBackground,
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
