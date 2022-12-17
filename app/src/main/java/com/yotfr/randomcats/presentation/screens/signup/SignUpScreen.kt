package com.yotfr.randomcats.presentation.screens.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.screens.signup.event.SignUpEvent
import com.yotfr.randomcats.presentation.screens.signup.event.SignUpScreenEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    backToSignIn: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.event.collect {
                when (it) {
                    SignUpScreenEvent.ShowUserAlreadyExistsSnackbar -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = context.resources.getString(R.string.user_already_exists)
                            )
                        }
                    }
                    SignUpScreenEvent.UserSuccessfullyCreated -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = context.resources.getString(R.string.account_successfully_created)
                            )
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            backToSignIn()
                        }
                    ) {
                        Icon(
                            Icons.Outlined.ArrowBack,
                            contentDescription = stringResource(id = R.string.to_sign_in)
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
                    start = dimensionResource(id = R.dimen.large_padding),
                    end = dimensionResource(id = R.dimen.large_padding)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )
            SignUpTitle(
                modifier = Modifier.fillMaxWidth(),
                titleText = stringResource(id = R.string.create_account),
                subTitleText = stringResource(id = R.string.fill_input_below)
            )
            Spacer(
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
                hidePasswordText = stringResource(id = R.string.hide_password),
                emailText = state.emailText,
                passwordText = state.passwordText,
                confirmPasswordText = state.confirmPasswordText,
                onEmailTextChanged = { newText ->
                    viewModel.onEvent(
                        SignUpEvent.UpdateEmailText(
                            newText = newText
                        )
                    )
                },
                onPasswordTextChanged = { newText ->
                    viewModel.onEvent(
                        SignUpEvent.UpdatePasswordText(
                            newText = newText
                        )
                    )
                },
                onConfirmPasswordTextChanged = { newText ->
                    viewModel.onEvent(
                        SignUpEvent.UpdateConfirmPasswordText(
                            newText = newText
                        )
                    )
                },
                isEmailInvalidError = state.isEmailInvalidError,
                isEmailEmptyError = state.isEmailEmptyError,
                isPasswordEmptyError = state.isPasswordEmptyError,
                emptyEmailFieldErrorMessage = stringResource(id = R.string.enter_email_adress),
                emptyPasswordFieldErrorMessage = stringResource(id = R.string.enter_password),
                invalidEmailErrorMessage = stringResource(id = R.string.email_adress_not_valid),
                isUserAlreadyExistsError = state.isUserAlreadyExistsException,
                isPasswordsNotMatchError = state.isPasswordsNotMatchError,
                isPasswordTooShortError = state.isPasswordTooShortError,
                passwordTooShortErrorMessage = stringResource(id = R.string.password_too_short),
                passwordsNotMatchErrorMessage = stringResource(id = R.string.passwords_do_not_match),
                usernameText = state.usernameText,
                usernameLabelText = stringResource(id = R.string.user_name),
                isUserNameEmptyError = state.isUserNameEmptyError,
                userNameEmptyErrorMessage = stringResource(id = R.string.enter_username),
                onUserNameTextChanged = { newText ->
                    viewModel.onEvent(
                        SignUpEvent.UpdateUserNameText(
                            newText = newText
                        )
                    )
                }
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            )
            SignUpButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp),
                signUpButtonText = stringResource(id = R.string.sign_up),
                onSignUpClicked = {
                    viewModel.onEvent(SignUpEvent.SignUpUser)
                },
                isLoading = state.isLoading
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            ToSignInText(
                modifier = Modifier.fillMaxWidth(),
                toSignInText = stringResource(id = R.string.already_have_acc),
                toSignInButtonText = stringResource(id = R.string.sign_in),
                toSignInClicked = {
                    backToSignIn()
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
fun SignUpInputFields(
    modifier: Modifier,
    emailLabelText: String,
    passwordLabelText: String,
    confirmPasswordLabelText: String,
    showPasswordText: String,
    hidePasswordText: String,
    emailText: String,
    passwordText: String,
    confirmPasswordText: String,
    onEmailTextChanged: (text: String) -> Unit,
    onPasswordTextChanged: (text: String) -> Unit,
    onConfirmPasswordTextChanged: (text: String) -> Unit,
    isEmailEmptyError: Boolean,
    isPasswordEmptyError: Boolean,
    isEmailInvalidError: Boolean,
    isUserAlreadyExistsError: Boolean,
    isPasswordsNotMatchError: Boolean,
    isPasswordTooShortError: Boolean,
    emptyEmailFieldErrorMessage: String,
    emptyPasswordFieldErrorMessage: String,
    invalidEmailErrorMessage: String,
    passwordTooShortErrorMessage: String,
    passwordsNotMatchErrorMessage: String,
    usernameText: String,
    usernameLabelText: String,
    isUserNameEmptyError: Boolean,
    userNameEmptyErrorMessage: String,
    onUserNameTextChanged: (text: String) -> Unit
) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var confirmPasswordHidden by rememberSaveable { mutableStateOf(true) }

    var emailTextFocused by remember { mutableStateOf(false) }
    var passwordTextFocused by remember { mutableStateOf(false) }
    var confirmPasswordTextFocused by remember { mutableStateOf(false) }
    var userNameTextFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    userNameTextFocused = it.isFocused
                },
            colors = TextFieldDefaults.textFieldColors(
                textColor = if (isUserNameEmptyError || isUserAlreadyExistsError) {
                    MaterialTheme.colorScheme.error
                } else MaterialTheme.colorScheme.onBackground,
                containerColor = if (isUserNameEmptyError || isUserAlreadyExistsError) {
                    MaterialTheme.colorScheme.error.copy(
                        alpha = 0.12f
                    )
                } else if (userNameTextFocused) {
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
            value = usernameText,
            onValueChange = { onUserNameTextChanged(it) },
            label = {
                Text(
                    text = usernameLabelText,
                    style = if (userNameTextFocused) MaterialTheme.typography.bodyMedium
                    else MaterialTheme.typography.bodyLarge
                )
            },
            isError = isUserNameEmptyError || isUserAlreadyExistsError,
            singleLine = true,
            supportingText = {
                Row(
                    modifier = Modifier.padding(
                        start = 52.dp
                    )
                ) {
                    if (isUserNameEmptyError) {
                        Text(text = userNameEmptyErrorMessage)
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
                    Icon(Icons.Outlined.Person, contentDescription = usernameLabelText)
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
                    emailTextFocused = it.isFocused
                },
            colors = TextFieldDefaults.textFieldColors(
                textColor = if (isEmailEmptyError || isEmailInvalidError || isUserAlreadyExistsError) {
                    MaterialTheme.colorScheme.error
                } else MaterialTheme.colorScheme.onBackground,
                containerColor = if (isEmailEmptyError || isEmailInvalidError || isUserAlreadyExistsError) {
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
            isError = isEmailEmptyError || isEmailInvalidError || isUserAlreadyExistsError,
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
                textColor = if (isPasswordEmptyError || isPasswordTooShortError || isUserAlreadyExistsError) {
                    MaterialTheme.colorScheme.error
                } else MaterialTheme.colorScheme.onBackground,
                containerColor = if (isPasswordEmptyError || isPasswordTooShortError || isUserAlreadyExistsError) {
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
            isError = isPasswordEmptyError || isPasswordTooShortError || isUserAlreadyExistsError,
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
                        start = 52.dp
                    )
                ) {
                    if (isPasswordEmptyError) {
                        Text(text = emptyPasswordFieldErrorMessage)
                    }
                    if (isPasswordTooShortError) {
                        Text(text = passwordTooShortErrorMessage)
                    }
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
                    confirmPasswordTextFocused = it.isFocused
                },
            colors = TextFieldDefaults.textFieldColors(
                textColor = if (isPasswordsNotMatchError || isUserAlreadyExistsError) {
                    MaterialTheme.colorScheme.error
                } else MaterialTheme.colorScheme.onBackground,
                containerColor = if (isPasswordsNotMatchError || isUserAlreadyExistsError) {
                    MaterialTheme.colorScheme.error.copy(
                        alpha = 0.12f
                    )
                } else if (confirmPasswordTextFocused) {
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
            value = confirmPasswordText,
            onValueChange = { onConfirmPasswordTextChanged(it) },
            label = {
                Text(
                    text = confirmPasswordLabelText,
                    style = if (confirmPasswordTextFocused) MaterialTheme.typography.bodyMedium
                    else MaterialTheme.typography.bodyLarge
                )
            },
            isError = isPasswordsNotMatchError || isUserAlreadyExistsError,
            visualTransformation =
            if (confirmPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            leadingIcon = {
                Row(
                    modifier = Modifier.padding(
                        start = 32.dp,
                        end = 8.dp
                    )
                ) {
                    Icon(Icons.Outlined.Lock, contentDescription = confirmPasswordLabelText)
                }
            },
            trailingIcon = {
                if (confirmPasswordText.isNotBlank()) {
                    Row(
                        modifier = Modifier.padding(
                            end = 32.dp,
                            start = 8.dp
                        )
                    ) {
                        IconButton(onClick = { confirmPasswordHidden = !confirmPasswordHidden }) {
                            val visibilityIcon =
                                if (confirmPasswordHidden) Icons.Outlined.Visibility else Icons.Filled.VisibilityOff
                            val description =
                                if (confirmPasswordHidden) showPasswordText else hidePasswordText
                            Icon(imageVector = visibilityIcon, contentDescription = description)
                        }
                    }
                }
            },
            supportingText = {
                Row(
                    modifier = Modifier.padding(
                        start = 52.dp
                    )
                ) {
                    if (isPasswordsNotMatchError) {
                        Text(text = passwordsNotMatchErrorMessage)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun SignUpButton(
    modifier: Modifier,
    signUpButtonText: String,
    onSignUpClicked: () -> Unit,
    isLoading: Boolean
) {
    Button(
        modifier = modifier,
        onClick = {
            onSignUpClicked()
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
                    text = signUpButtonText.uppercase(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun ToSignInText(
    modifier: Modifier,
    toSignInText: String,
    toSignInButtonText: String,
    toSignInClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.alpha(0.5f),
            color = MaterialTheme.colorScheme.onBackground,
            text = toSignInText,
            style = MaterialTheme.typography.titleMedium
        )
        TextButton(
            onClick = { toSignInClicked() }
        ) {
            Text(
                text = toSignInButtonText,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
