package com.yotfr.randomcats.presentation.screens.settingstheme

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.screens.settingstheme.event.SettingsThemeEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsThemeScreen(
    goBack: () -> Unit,
    viewModel: SettingsThemeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            SettingsThemeTopBar(
                onArrowBackPressed = { goBack() },
                topBarTitle = stringResource(id = R.string.theme)
            )
        }
    ) {
        ThemePickerCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            currentTheme = state.currentTheme,
            onThemeSelected = { theme ->
                viewModel.onEvent(
                    SettingsThemeEvent.ThemeChanged(
                        theme = theme
                    )
                )
            },
            lightThemeText = stringResource(id = R.string.light),
            darkThemeText = stringResource(id = R.string.dark),
            systemDefaultThemeText = stringResource(id = R.string.system_default),
            selectedThemeIcon = Icons.Outlined.CheckCircle
        )
    }
}
