package com.yotfr.randomcats.presentation.screens.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.screens.signup.event.SignUpEvent
import com.yotfr.randomcats.presentation.screens.signup.event.SignUpScreenEvent
import com.yotfr.randomcats.presentation.spacing
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
                title = {},
                windowInsets = WindowInsets.systemBars
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = MaterialTheme.spacing.large,
                    end = MaterialTheme.spacing.large
                )
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.spacing.medium)
            )
            SignUpTitle(
                modifier = Modifier.fillMaxWidth(),
                titleText = stringResource(id = R.string.create_account),
                subTitleText = stringResource(id = R.string.fill_input_below)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.spacing.large)
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
                    .height(MaterialTheme.spacing.large)
            )
            SignUpButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.extraLarge),
                signUpButtonText = stringResource(id = R.string.sign_up),
                onSignUpClicked = {
                    viewModel.onEvent(SignUpEvent.SignUpClicked)
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
