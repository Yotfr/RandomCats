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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.screens.sign_up.event.SignUpEvent
import com.yotfr.randomcats.presentation.screens.sign_up.event.SignUpScreenEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    backToSignIn:() -> Unit,
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
                        backToSignIn()
                    }
                }
            }
        }
    }

    Scaffold(
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
                    bottom = 32.dp,
                    start = 32.dp,
                    end = 32.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
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
                passwordsNotMatchErrorMessage = stringResource(id = R.string.passwords_do_not_match)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            )
            SignUpButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 52.dp),
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
) {

    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var confirmPasswordHidden by rememberSaveable { mutableStateOf(true) }


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = emailText,
            onValueChange = { onEmailTextChanged(it) },
            label = { Text(text = emailLabelText) },
            isError = isEmailEmptyError || isEmailInvalidError || isUserAlreadyExistsError,
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
            isError = isPasswordEmptyError || isPasswordTooShortError,
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
                if (isPasswordTooShortError) {
                    Text(text = passwordTooShortErrorMessage)
                }
            }
        )
        OutlinedTextField(
            value = confirmPasswordText,
            onValueChange = { onConfirmPasswordTextChanged(it) },
            label = { Text(text = confirmPasswordLabelText) },
            isError = isPasswordsNotMatchError,
            visualTransformation =
            if (confirmPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            leadingIcon = {
                Icon(
                    Icons.Outlined.Lock,
                    contentDescription = confirmPasswordLabelText
                )
            },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordHidden = !confirmPasswordHidden }) {
                    val visibilityIcon =
                        if (confirmPasswordHidden) Icons.Outlined.Visibility else Icons.Filled.VisibilityOff
                    val description =
                        if (confirmPasswordHidden) showPasswordText else hidePasswordText
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                }
            },
            supportingText = {
                if (isPasswordsNotMatchError) {
                    Text(text = passwordsNotMatchErrorMessage)
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
    isLoading: Boolean
) {
    Button(
        modifier = modifier,
        onClick = {
            onSignUpClicked()
        }
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = signUpButtonText.uppercase(),
                style = MaterialTheme.typography.titleMedium
            )
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
            modifier = Modifier.alpha(0.7f),
            color = MaterialTheme.colorScheme.secondary,
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