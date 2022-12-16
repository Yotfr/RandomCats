package com.yotfr.randomcats.presentation.screens.resetpassword

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.screens.resetpassword.event.ResetPasswordEvent
import com.yotfr.randomcats.presentation.screens.resetpassword.event.ResetPasswordScreenEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    backToSignIn: () -> Unit,
    viewModel: ResetPasswordViewModel = hiltViewModel()
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
                    ResetPasswordScreenEvent.ShowNoUserCorrespondingEmailSnackbar -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = context.resources.getString(R.string.no_user_with_this_email)
                            )
                        }
                    }
                    ResetPasswordScreenEvent.ShowEmailLinkSentSnackbar -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = context.resources.getString(R.string.check_email)
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
            ResetPasswordTitle(
                modifier = Modifier.fillMaxWidth(),
                titleText = stringResource(id = R.string.reset_password),
                subTitleText = stringResource(id = R.string.reset_password_subtitle)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            )
            EmailField(
                isEmailEmptyError = state.isEmailEmptyError,
                emailLabelText = stringResource(id = R.string.email),
                modifier = Modifier.fillMaxWidth(),
                onEmailTextChanged = { newText ->
                    viewModel.onEvent(
                        ResetPasswordEvent.UpdateEmailText(
                            newText = newText
                        )
                    )
                },
                emailText = state.emailText,
                emptyEmailFieldErrorMessage = stringResource(id = R.string.enter_email_adress),
                isEmailInvalidError = state.isEmailInvalidError,
                invalidEmailErrorMessage = stringResource(id = R.string.email_adress_not_valid),
                isNoUserWithThisEmailError = state.isNoUserWithThisEmailError
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            )
            ResetPasswordButton(
                onSendClicked = {
                    viewModel.onEvent(ResetPasswordEvent.SendResetEmail)
                },
                sendButtonText = stringResource(id = R.string.send),
                isLoading = state.isLoading
            )
        }
    }
}

@Composable
fun ResetPasswordTitle(
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
fun EmailField(
    isEmailEmptyError: Boolean,
    emailLabelText: String,
    modifier: Modifier,
    onEmailTextChanged: (text: String) -> Unit,
    emailText: String,
    emptyEmailFieldErrorMessage: String,
    isEmailInvalidError: Boolean,
    invalidEmailErrorMessage: String,
    isNoUserWithThisEmailError:Boolean
) {
    var emailTextFocused by remember { mutableStateOf(false) }

    TextField(
        modifier = modifier
            .onFocusChanged {
                emailTextFocused = it.isFocused
            },
        colors = TextFieldDefaults.textFieldColors(
            textColor = if (isEmailEmptyError || isEmailInvalidError || isNoUserWithThisEmailError) {
                MaterialTheme.colorScheme.error
            } else MaterialTheme.colorScheme.onBackground,
            containerColor = if (isEmailEmptyError || isEmailInvalidError || isNoUserWithThisEmailError) {
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
        isError = isEmailEmptyError || isEmailInvalidError || isNoUserWithThisEmailError,
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
}

@Composable
fun ResetPasswordButton(
    onSendClicked: () -> Unit,
    sendButtonText: String,
    isLoading: Boolean
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 64.dp
            ),
        onClick = {
            onSendClicked()
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
                    text = sendButtonText.uppercase(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}