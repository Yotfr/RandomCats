package com.yotfr.randomcats.presentation.screens.resetpassword

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
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
            ResetPasswordEmailField(
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
                    viewModel.onEvent(ResetPasswordEvent.ResetEmailClicked)
                },
                sendButtonText = stringResource(id = R.string.send),
                isLoading = state.isLoading
            )
        }
    }
}
