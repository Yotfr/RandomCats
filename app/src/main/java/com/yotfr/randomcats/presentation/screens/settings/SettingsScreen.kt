package com.yotfr.randomcats.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.screens.settings.event.SettingsEvent
import com.yotfr.randomcats.presentation.screens.settings.event.SettingsScreenEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateToAuth: () -> Unit,
    navigateToThemeScreen:() -> Unit,
    goBack: () -> Unit
) {

    val event = viewModel.event

    val lifecycle = LocalLifecycleOwner.current.lifecycle


    Scaffold(
        topBar = {
            TopBar(
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
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 8.dp
                )
        ) {
            AccountSection(
                modifier = Modifier.fillMaxWidth(),
                accountTitleText = stringResource(id = R.string.account),
                onChangePasswordClicked = {  },
                changePasswordText = stringResource(id = R.string.change_password)
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))
            AppearanceSection(
                modifier = Modifier.fillMaxWidth(),
                appearanceTitleText = stringResource(id = R.string.appearance),
                onThemeClicked = { viewModel.onEvent(SettingsEvent.ThemePressed) },
                themeText = stringResource(id = R.string.theme)
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))
            GeneralSection(
                modifier = Modifier.fillMaxWidth(),
                generalTitleText = stringResource(id = R.string.general),
                onLanguageClicked = {  },
                languageText = stringResource(id = R.string.language)
            )
            LogOutButton (
                onLogoutClicked = {  },
                logoutText = stringResource(id = R.string.sign_out)
            )
        }
    }


    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            event.collect {
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onArrowBackPressed: () -> Unit,
    topBarTitle: String
) {
    TopAppBar(
        title = {
            Text(text = topBarTitle)
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onArrowBackPressed()
                }
            ) {
                Icon(
                    Icons.Outlined.ArrowBack,
                    contentDescription = stringResource(id = R.string.to_sign_in)
                )
            }
        }
    )
}

@Composable
fun AccountSection(
    modifier: Modifier,
    accountTitleText: String,
    onChangePasswordClicked: () -> Unit,
    changePasswordText: String
) {
    ElevatedCard(modifier = modifier) {
        Text(
            text = accountTitleText,
            color = MaterialTheme.colorScheme.primary
        )
        TextButton(
            onClick = { onChangePasswordClicked() }
        ) {
            Text(text = changePasswordText)
        }
    }
}

@Composable
fun AppearanceSection(
    modifier: Modifier,
    appearanceTitleText: String,
    onThemeClicked: () -> Unit,
    themeText: String
) {
    ElevatedCard(modifier = modifier) {
        Text(
            text = appearanceTitleText,
            color = MaterialTheme.colorScheme.primary
        )
        TextButton(
            onClick = { onThemeClicked() }
        ) {
            Text(text = themeText)
        }
    }
}

@Composable
fun GeneralSection(
    modifier: Modifier,
    generalTitleText: String,
    onLanguageClicked: () -> Unit,
    languageText: String
) {
    ElevatedCard(modifier = modifier) {
        Text(
            text = generalTitleText,
            color = MaterialTheme.colorScheme.primary
        )
        TextButton(
            onClick = { onLanguageClicked() }
        ) {
            Text(text = languageText)
        }
    }
}

@Composable
fun LogOutButton(
    onLogoutClicked: () -> Unit,
    logoutText: String
) {
    TextButton(onClick = {
        onLogoutClicked()
    }) {
        Text(text = logoutText)
    }
}