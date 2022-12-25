package com.yotfr.randomcats.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.screens.settings.event.SettingsEvent
import com.yotfr.randomcats.presentation.screens.settings.event.SettingsScreenEvent
import com.yotfr.randomcats.presentation.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateToAuth: () -> Unit,
    navigateToThemeScreen: () -> Unit,
    goBack: () -> Unit
) {
    val uiEvent = viewModel.uiEvent
    val state by viewModel.state.collectAsState()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    Scaffold(
        topBar = {
            SettingsTopBar(
                onArrowBackPressed = {
                    goBack()
                },
                topBarTitle = stringResource(id = R.string.settings)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    start = MaterialTheme.spacing.default,
                    end = MaterialTheme.spacing.default,
                    bottom = MaterialTheme.spacing.small
                )
        ) {
            AccountSection(
                modifier = Modifier.fillMaxWidth(),
                userNameText = state.userName,
                emailText = state.email,
                defaultProfileImagePainter = painterResource(id = R.drawable.cat_log)
            )
            AppearanceSection(
                modifier = Modifier.fillMaxWidth(),
                appearanceTitleText = stringResource(id = R.string.appearance),
                onThemeClicked = { viewModel.onEvent(SettingsEvent.ThemePressed) },
                themeText = stringResource(id = R.string.theme),
                themeIcon = Icons.Outlined.Palette,
                chevronForwardIcon = Icons.Outlined.ChevronRight
            )
            LogOutButton(
                onLogoutClicked = {
                    viewModel.onEvent(SettingsEvent.SignOutPressed)
                },
                logoutText = stringResource(id = R.string.sign_out)
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEvent.collect {
                when (it) {
                    SettingsScreenEvent.NavigateToAuth -> {
                        navigateToAuth()
                    }
                    SettingsScreenEvent.NavigateToThemeScreen -> {
                        navigateToThemeScreen()
                    }
                }
            }
        }
    }
}
